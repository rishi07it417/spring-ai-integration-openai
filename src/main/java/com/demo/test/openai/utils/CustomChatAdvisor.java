package com.demo.test.openai.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public class CustomChatAdvisor implements CallAdvisor {
    private static final Logger log =
            LoggerFactory.getLogger(CustomChatAdvisor.class);

    @Override
    public String getName() {
        return "custom-logging-advisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request,CallAdvisorChain chain) {

        log.info("Before AI call");

        // Read user prompt
        String userText = request.prompt().getUserMessage().getText();

        log.info("User Prompt: {}", userText);

        // Add custom context/metadata
        request.context().put("request-start-time",
                System.currentTimeMillis());

        // Continue advisor chain
        ChatClientResponse response = chain.nextCall(request);

        log.info("After AI call");

        String content = response.chatResponse()
                .getResult()
                .getOutput()
                .getText();

        log.info("AI Response: {}", content);

        return response;
    }
}
