package org.frostnova.aigateway.domain.model;

import lombok.Data;

import java.util.List;

/**
 * 中台发给大模型底层的基础请求结构（对齐 OpenAI 标准）
 */
@Data
public class LlmRequest {
    private String model;
    private List<Message> messages;

    public void addMessage(String source, String messageBody) {
        Message message = new Message();
        message.setSource(source);
        message.setBody(messageBody);
        messages.add(message);
    }

}
