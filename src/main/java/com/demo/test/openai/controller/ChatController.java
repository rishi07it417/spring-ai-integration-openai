package com.demo.test.openai.controller;

import com.demo.test.openai.model.AppChatResponse;
import com.demo.test.openai.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public AppChatResponse chat(@RequestHeader(name = "userId", required = true) final String userId,
            @RequestParam(name = "message", required = true) final String message) {
       return this.chatService.chat(userId, message);
    }

    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithFluentApiAndGetStream(@RequestHeader(name = "userId", required = true) final String userId,
                                                      @RequestParam(name = "message", required = true) final String message,
                                                      @RequestParam(name = "role", required = true) final String role) {
        return this.chatService.chatWithFluentApiAndGetStream(userId, message, role);
    }

    @GetMapping("/chatWithPromptTemplate")
    public AppChatResponse chatWithPromptTemplate(@RequestHeader(name = "userId", required = true) final String userId,
            @RequestParam(name = "message", required = true) final String message,
            @RequestParam(name = "role", required = true) final String role) {
        return this.chatService.chatWithPromptTemplate(userId, message, role);
    }

    @GetMapping("/chatWithFluentApi")
    public AppChatResponse chatWithFluentApi(@RequestHeader(name = "userId", required = true) final String userId,
            @RequestParam(name = "message", required = true) final String message,
            @RequestParam(name = "role", required = true) final String role) {
        return this.chatService.chatWithFluentApi(userId, message, role);
    }
}
