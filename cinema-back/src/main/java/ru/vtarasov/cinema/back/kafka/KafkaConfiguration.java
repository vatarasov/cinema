package ru.vtarasov.cinema.back.kafka;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
    @Bean(destroyMethod = "close")
    public KafkaConsumer<String, Serializable> kafkaConsumer(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                                                             @Value("${spring.kafka.consumer.enable-auto-commit}") boolean enableAutoCommit,
                                                             @Value("${spring.kafka.consumer.group-id}") String groupId,
                                                             @Value("${order.events.topic}") String orderEventsTopic) {
        KafkaConsumer<String, Serializable> kafkaConsumer = new KafkaConsumer<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class
        ));
        kafkaConsumer.subscribe(List.of(orderEventsTopic));
        return kafkaConsumer;
    }
}
