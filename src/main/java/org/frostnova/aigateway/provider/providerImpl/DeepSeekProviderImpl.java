package org.frostnova.aigateway.provider.providerImpl;

import org.frostnova.aigateway.domain.model.LlmRequest;
import org.frostnova.aigateway.domain.model.LlmResponse;
import org.frostnova.aigateway.provider.LlmProvider;
import org.springframework.stereotype.Component;

@Component
public class DeepSeekProviderImpl implements LlmProvider {
    @Override
    public String providerName() { return "deepseek"; }

    @Override
    public LlmResponse chat(LlmRequest request) {
        // TODO: 组装 HTTP 请求发给 DeepSeek API
        return new LlmResponse();
    }
}
