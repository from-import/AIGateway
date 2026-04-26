package org.frostnova.aigateway.common.scattergather.repository;

import org.frostnova.aigateway.common.scattergather.entity.ParentTask;
import org.springframework.stereotype.Repository;

@Repository
public class ParentTaskRepository {

    // 1. 数据库层原子累加（完美避免 while+CAS 的性能风暴）
    public void incrementFinishedCount(String parentId) {
        // SQL: UPDATE parent_task SET finished_count = finished_count + 1 WHERE id = #{parentId}
    }

    // 2. 状态机单次 CAS 截断（保证全局只触发一次回调）
    // 返回值是受影响的行数 (1表示抢占成功，0表示被别人抢了)
    public int casCompleteStatus(String parentId) {
        // SQL: UPDATE parent_task SET status = 'COMPLETED' WHERE id = #{parentId} AND status = 'PROCESSING'
        return 1; // Mock 默认抢占成功
    }

    // 3. 获取任务当前状态 (供检查进度使用)
    public ParentTask getById(String parentId) {
        // Mock
        return new ParentTask();
    }
}
