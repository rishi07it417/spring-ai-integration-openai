package com.demo.test.openai.controller;

import com.demo.test.openai.model.AppChatResponse;
import com.demo.test.openai.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public AppChatResponse chat(@RequestParam(name = "message", required = true) String message) {
       return this.chatService.chat(message);
    }

    @GetMapping("/chatWithPromptTemplate")
    public AppChatResponse chatWithPromptTemplate(@RequestParam(name = "message", required =
            true) String message, @RequestParam(name = "role", required = true) String role) {
        return this.chatService.chatWithPromptTemplate(message, role);
    }

    @GetMapping("/chatWithFluentApi")
    public AppChatResponse chatWithFluentApi(@RequestParam(name = "message", required =
            true) String message, @RequestParam(name = "role", required = true) String role) {
        return this.chatService.chatWithFluentApi(message, role);
    }
}
