package com.demo.test.openai.service;

import com.demo.test.openai.model.AppChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient customChatClient;
    private final Resource systemPrompt;
    private final Resource systemStreamPrompt;

    private Logger logger = LoggerFactory.getLogger(ChatService.class);

    ChatService(@Qualifier("customChatClient") ChatClient customChatClient,
                @Value("classpath:prompts/AppSystemPrompt.st") Resource systemPrompt,
                @Value("classpath:prompts/AppSystemStreamPrompt.st") Resource systemStreamPrompt) {
        this.customChatClient = customChatClient;
        this.systemPrompt = systemPrompt;
        this.systemStreamPrompt = systemStreamPrompt;
    }

    public AppChatResponse chat(final String userId, final String message) {
        AppChatResponse content = this.customChatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.params(Map.of("useId", userId)))
                .system("  Return valid JSON with:\n" +
                        "            - title\n" +
                        "            - content")
                .user(message).call().entity(AppChatResponse.class);
        logger.info("Chat response: " + content.toString());
        return content;
    }

    public AppChatResponse chatWithPromptTemplate(final String userId, final String message, final String role) {
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
                .advisors(advisorSpec -> advisorSpec.params(Map.of("useId", userId)))
                .user(userMessage)
                .system(systemMessage)
                .call().entity(AppChatResponse.class);
        logger.info("PROMPT TEMPLATE Chat response: " + content.toString());
        return content;
    }

    public AppChatResponse chatWithFluentApi(final String userId, final String message, final String role) {

        // Call the ChatClient with the combined prompt
        AppChatResponse content = this.customChatClient
                .prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.params(Map.of("useId", userId)))
                .system(promptSystemSpec ->
                        promptSystemSpec.text(this.systemPrompt)
                             .params(Map.of("role", role)))
                .call().entity(AppChatResponse.class);
        logger.info("Fluent API Chat response: " + content.toString());
        return content;
    }

    public Flux<String> chatWithFluentApiAndGetStream(final String userId, final String message, final String role) {

        // Call the ChatClient with the combined prompt
        return  this.customChatClient
                .prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.params(Map.of("useId", userId)))
                .system(promptSystemSpec ->
                        promptSystemSpec.text(this.systemStreamPrompt)
                                .params(Map.of("role", role)))
                .stream()
                .content();

    }
}
