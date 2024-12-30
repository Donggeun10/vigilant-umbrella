package com.example.demo.config;


import java.io.IOException;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class ChatConfig {

    @Value("classpath:prompts/system.st")
    Resource systemPrompt;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, MessageChatMemoryAdvisor chatMemory) throws IOException {

        return builder.defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
            .defaultAdvisors(new SimpleLoggerAdvisor(), chatMemory)
            .build();

    }

    @Bean
    public MessageChatMemoryAdvisor chatMemory() {
        return new MessageChatMemoryAdvisor(new InMemoryChatMemory());
    }
}
