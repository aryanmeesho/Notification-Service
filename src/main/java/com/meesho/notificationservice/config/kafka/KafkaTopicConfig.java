package com.meesho.notificationservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaTopicConfig {
    Logger log = LoggerFactory.getLogger(KafkaTopicConfig.class);
    @Value("${kafka.topic}")
    private String TOPIC;

    @Bean
    public NewTopic createTopic() {
        log.info("Kafka Admin connected.");
        log.info("Topic initialized in kafka.");
        return TopicBuilder.name(TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
