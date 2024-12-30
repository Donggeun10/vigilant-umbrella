package com.example.demo.controller;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Slf4j
@RestController
class ChatController {

    private final ChatClient chatClient;

    @GetMapping(value = "/random-uuid")
    public String chat() {

        return UUID.randomUUID().toString();
    }

    @PostMapping("/chat/streaming/{user}")
    public Flux<String> generation(@PathVariable UUID user, @RequestBody String userInput) {
        log.debug("User input: {}", userInput);
        return this.chatClient.prompt()
            .user(userInput)
            .advisors(a -> a
                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, user.toString())
                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
            .stream()
            .content();
    }
}
