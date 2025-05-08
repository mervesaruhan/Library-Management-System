package com.mervesaruhan.librarymanagementsystem.kafka.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;
import com.mervesaruhan.librarymanagementsystem.kafka.dto.ErrorLogMessage;
import com.mervesaruhan.librarymanagementsystem.kafka.dto.RequestLogMessage;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConfigs() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "log-consumer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, RequestLogMessage> requestLogConsumerFactory() {
        final JsonDeserializer<RequestLogMessage> deserializer = new JsonDeserializer<>(RequestLogMessage.class);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(baseConfigs(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RequestLogMessage> requestLogKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, RequestLogMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestLogConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ErrorLogMessage> errorLogConsumerFactory() {
        final JsonDeserializer<ErrorLogMessage> deserializer = new JsonDeserializer<>(ErrorLogMessage.class);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(baseConfigs(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ErrorLogMessage> errorLogKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, ErrorLogMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(errorLogConsumerFactory());
        return factory;
    }
}