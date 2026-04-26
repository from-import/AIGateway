package org.frostnova.aigateway.common.scattergather.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTaskMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 全链路追踪 ID
     * 从网关收到 HTTP 请求那一刻生成的 traceId，必须通过 MQ 传给消费者
     */
    private String traceId;

    /** 父任务 ID */
    private String parentId;

    /** 当前子任务 ID */
    private String subTaskId;

    /**
     * 需要被处理的具体数据 (Prompt / Document Chunk)
     * 注意：如果 payload 极大(>几MB)，建议这里传 OSS 的 URL，避免把 MQ 撑爆
     */
    private String payload;

    /** 路由策略：指定要调用的 LLM 模型 (例如: "deepseek-chat", "gpt-4") */
    private String providerModel;

    /** 当前重试次数（用于防毒消息 Poison Pill） */
    private int retryCount;

    /** 消息产生的时间戳（用于监控 MQ 延迟 / 堆积报警） */
    private long timestamp;
}
