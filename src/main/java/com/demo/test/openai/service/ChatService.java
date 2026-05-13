package com.demo.test.openai.service;

import com.demo.test.openai.model.AppChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient customChatClient;

    ChatService(@Qualifier("customChatClient") ChatClient customChatClient) {
        this.customChatClient = customChatClient;
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
                .template("  Act as a {role} and answer the question based on your expertise.\n" )
                .template("  Return valid JSON with:\n" +
                        "            - title\n" +
                        "            - content")
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
                        promptSystemSpec.text(

                        "  Act as a {role} and answer the question based on your expertise in bullet points.\n" +
                        "   Return valid JSON with:\n" +
                        "            - title\n" +
                        "            - content")
                             .params(Map.of("role", role)))
                .call().entity(AppChatResponse.class);
        System.out.println("Fluent API Chat response: " + content.toString());
        return content;
    }
}
