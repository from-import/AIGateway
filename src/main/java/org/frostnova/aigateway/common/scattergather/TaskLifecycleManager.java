package org.frostnova.aigateway.common.scattergather;

import lombok.extern.slf4j.Slf4j;
import org.frostnova.aigateway.common.scattergather.MQ.TaskMQ;
import org.frostnova.aigateway.common.scattergather.entity.ParentTask;
import org.frostnova.aigateway.common.scattergather.entity.SubTaskMessage;
import org.frostnova.aigateway.common.scattergather.enums.CallbackActionEnum;
import org.frostnova.aigateway.common.scattergather.event.TaskCompletedEvent;
import org.frostnova.aigateway.common.scattergather.repository.ParentTaskRepository;
import org.frostnova.aigateway.common.scattergather.repository.SubTaskRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskLifecycleManager {

    private static final String LLM_TASK_TOPIC = "ai.gateway.llm.task.queue";

    private final ParentTaskRepository parentTaskRepo;
    private final SubTaskRepository subTaskRepo;
    private final TaskMQ taskMq;
    private final ApplicationEventPublisher eventPublisher;

    public TaskLifecycleManager(ParentTaskRepository parentTaskRepo, TaskMQ taskMq, ApplicationEventPublisher eventPublisher, SubTaskRepository subTaskRepo) {
        this.parentTaskRepo = parentTaskRepo;
        this.taskMq = taskMq;
        this.eventPublisher = eventPublisher;
        this.subTaskRepo = subTaskRepo;
    }

    /**
     * 生命周期一：任务初始化与发散 (Scatter)
     * 接收业务端大任务，落库拆分，并丢入 MQ
     *
     * @param payloads 大任务被切分后的多段文本/Prompt列表
     * @param action   后续动作类型
     * @param target   动作目标 (如 Webhook URL)
     * @return 返回生成的 parentId，可直接响应给 HTTP 客户端
     */
    @Transactional(rollbackFor = Exception.class)
    public String initTask(List<String> payloads, CallbackActionEnum action, String target) {
        String parentId = "TASK-" + UUID.randomUUID();
        int totalCount = payloads.size();

        log.info("【任务初始化】ParentId: {}, Total: {}, Action: {}", parentId, totalCount, action.getCode());

        // 1. 落库 ParentTask (status=PROCESSING, count=0)
        // parentTaskRepo.insert(new ParentTask(parentId, totalCount, action.getCode(), target));

        // 2. 落库 SubTasks 并且投递至 MQ
        for (int i = 0; i < payloads.size(); i++) {
            String subTaskId = parentId + "-SUB-" + i;
            String payload = payloads.get(i);

            // 落库 SubTask (status=PENDING)
            // subTaskRepo.insert(new SubTask(subTaskId, parentId, payload));

            // 投递到 MQ 进行异步并发消费 (延迟0ms)
            SubTaskMessage msg = new SubTaskMessage();
            taskMq.produce(LLM_TASK_TOPIC, msg, 0);
        }

        return parentId;
    }

    /**
     * 生命周期二：任务回调推进与汇聚终态拦截 (Gather)
     * 供 MQ 消费者在完成单次大模型调用后触发
     *
     * @param parentId  父任务ID
     * @param subTaskId 子任务ID
     * @param llmResult 大模型返回的文本内容
     */
    public void onSubTaskComplete(String parentId, String subTaskId, String llmResult) {
        log.info("【子任务完成】ParentId: {}, SubTaskId: {}", parentId, subTaskId);

        // 1. 保存当前子任务的结果，状态置为 SUCCESS
        // subTaskRepo.updateResult(subTaskId, llmResult);

        // 2. 利用 DB 锁进行无锁原子累加 (规避 while+CAS 带来的 DB 连接耗尽)
        parentTaskRepo.incrementFinishedCount(parentId);

        // 3. 查询当前父任务整体进度
        ParentTask parent = parentTaskRepo.getById(parentId);

        // 4. 判断是否全部完成
        if (parent.getFinishedCount() >= parent.getTotalCount()) {
            log.info("【任务达标检测】ParentId: {} 所有子任务执行完毕，尝试抢占回调权...", parentId);

            // 5. 核心：终态 CAS 抢占。高并发下，只有成功把 PROCESSING 改为 COMPLETED 的那个线程，才是天选之子
            int updatedRows = parentTaskRepo.casCompleteStatus(parentId);

            if (updatedRows == 1) {
                log.info("【抢占成功】ParentId: {} 抢到了唯一回调权，即将触发事件路由: {}", parentId, parent.getAction().getCode());

                // 6. 扔出 Spring Event 解耦后续业务逻辑（交给 Router 层监听处理）
                eventPublisher.publishEvent(new TaskCompletedEvent(this, parentId, parent.getAction(), parent.getTarget()));
            } else {
                log.info("【抢占失败】ParentId: {} 状态已被其他并发线程修改，静默退出", parentId);
            }
        }
    }
}
