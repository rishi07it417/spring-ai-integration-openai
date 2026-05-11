package com.demo.test.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.demo.test.openai.contants.AppContants.*;

@Configuration
public class AppConfig {

    private final ChatClient.Builder chatClientBuilder;

    public AppConfig(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }


    @Bean
    public ChatClient customChatClient() {

        return this.chatClientBuilder
                .defaultOptions(ChatOptions.builder()
                        .model(OPENAI_MODEL))
                .build();
    }
}