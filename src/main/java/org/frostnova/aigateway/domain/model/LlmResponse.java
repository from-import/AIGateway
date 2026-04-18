package org.frostnova.aigateway.domain.model;

import lombok.Data;

/**
 * 模型返回的标准结构
 */
@Data
public class LlmResponse {
    /**
     * 返回文本
     */
    private String content;

    /**
     * input token消耗
     */
    private int promptTokens;

    /**
     * output token消耗
     */
    private int completionTokens;

    /**
     * 模型回答提供方name
     */
    private String providerName;
}
