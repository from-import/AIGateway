package org.frostnova.aigateway.common.scattergather.enums;

import lombok.Getter;

@Getter
public enum CallbackActionEnum {
    WEBHOOK_TRIGGER("WEBHOOK", "触发外部 Webhook 回调"),
    KNOWLEDGE_DB_INSERT("RAG_INSERT", "RAG场景：切片向量化后存入向量库"),
    SYSTEM_INTERNAL_LOG("LOG_ONLY", "仅打印内部审计日志，无业务动作");

    private final String code;
    private final String desc;

    CallbackActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 辅助方法：通过 code 获取枚举
    public static CallbackActionEnum fromCode(String code) {
        for (CallbackActionEnum action : CallbackActionEnum.values()) {
            if (action.getCode().equals(code)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown CallbackAction code: " + code);
    }
}
