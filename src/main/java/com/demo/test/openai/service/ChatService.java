package com.demo.test.openai.service;

import com.demo.test.openai.model.AppChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient customChatClient;
    private final Resource systemPrompt;

    ChatService(@Qualifier("customChatClient") ChatClient customChatClient,@Value("classpath:prompts/AppSystemPrompt.st") Resource systemPrompt) {
        this.customChatClient = customChatClient;
        this.systemPrompt = systemPrompt;
    }

    public AppChatResponse chat(String message) {
        AppChatResponse content = this.customChatClient
                .prompt()
                .system("  Return valid JSON with:\n" +
                        "            - title\n" +
                        "            - content")
                .user(message).call().entity(AppChatResponse.class);
        System.out.println("Chat response: " + content.toString());
        return content;
    }

    public AppChatResponse chatWithPromptTemplate(String message, String role) {
        // User PromptTemplate and SystemPromptTemplate to structure the conversation
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(message)
                .build();

        SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
                .resource(this.systemPrompt)
                .variables(Map.of("role", role))
                .build();

        // User and System Messages will be combined in the prompt
        var userMessage = promptTemplate.render();
        var systemMessage = systemPromptTemplate.render(Map.of("role", role));


        // Call the ChatClient with the combined prompt
        AppChatResponse content = this.customChatClient
                .prompt()
                .user(userMessage)
                .system(systemMessage)
                .call().entity(AppChatResponse.class);
        System.out.println("PROMPT TEMPLATE Chat response: " + content.toString());
        return content;
    }

    public AppChatResponse chatWithFluentApi(String message, String role) {

        // Call the ChatClient with the combined prompt
        AppChatResponse content = this.customChatClient
                .prompt()
                .user(message)
                .system(promptSystemSpec ->
                        promptSystemSpec.text(this.systemPrompt)
                             .params(Map.of("role", role)))
                .call().entity(AppChatResponse.class);
        System.out.println("Fluent API Chat response: " + content.toString());
        return content;
    }
}
