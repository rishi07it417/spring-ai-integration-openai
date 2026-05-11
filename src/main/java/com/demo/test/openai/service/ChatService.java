package com.demo.test.openai.service;

import com.demo.test.openai.model.AppChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
