package org.frostnova.aigateway.common.scattergather.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frostnova.aigateway.common.scattergather.enums.CallbackActionEnum;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentTask {

    /** 任务主键 ID (通常格式：业务前缀_雪花算法ID 或 UUID) */
    private String id;

    /** 租户/应用标识（★大厂必备：网关是多业务共享的，必须隔离计费和权限） */
    private String appId;

    /** 子任务总数 */
    private Integer totalCount;

    /** 已完成子任务数 */
    private Integer finishedCount;

    /** 任务状态：PROCESSING, COMPLETED, FAILED, TIMEOUT */
    private String status;

    /** 回调动作类型 */
    private CallbackActionEnum action;

    /** 回调目标 (URL 或 BeanName) */
    private String target;

    /**
     * 扩展字段 (JSON 格式)
     * 大厂必备：避免频繁修改表结构。如存储 Webhook 需要的鉴权 Header、重试策略等
     */
    private String extParams;

    /** 创建时间（用于清理过期任务和审计） */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
