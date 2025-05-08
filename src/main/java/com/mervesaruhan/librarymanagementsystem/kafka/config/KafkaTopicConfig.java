package com.mervesaruhan.librarymanagementsystem.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin.NewTopics createTopics() {
        final NewTopic errorLog = TopicBuilder.name("errorLog").build();
        final NewTopic requestLog = TopicBuilder.name("requestLog").build();
        return new KafkaAdmin.NewTopics(errorLog, requestLog);
    }
}