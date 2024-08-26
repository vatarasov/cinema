package ru.vtarasov.cinema.back.order.consumer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import generated.ru.vtarasov.cinema.avro.dto.OrderEventDto;
import generated.ru.vtarasov.cinema.avro.dto.OrderFinishedDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.vtarasov.cinema.back.util.BackLogger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class LogOrderEventConsumerTest {
    private static final String BUYER_NAME = "Vova";
    private static final String FILM = "Terminator";
    private static final int PRICE = 0;

    @Value("${order.events.topic}")
    private String orderEventsTopic;

    @Autowired
    private LogOrderEventConsumer logOrderEventConsumer;

    @MockBean
    private KafkaConsumer<String, Serializable> kafkaConsumer;

    @MockBean
    private BackLogger backLogger;

    @BeforeEach
    public void setUp() {
        clearInvocations(kafkaConsumer, backLogger);
    }

    @Test
    public void shouldConsumeOrderCreatedEvent() {
        prepareKafkaConsumer(new OrderEventDto(FILM, PRICE));

        logOrderEventConsumer.log();

        verify(backLogger).info("Order event received: {} = {}", BUYER_NAME, "film: Terminator, price: 0");
    }

    @Test
    public void shouldConsumeOrderPaidEvent() {
        prepareKafkaConsumer(new OrderFinishedDto("PAID"));

        logOrderEventConsumer.log();

        verify(backLogger).info("Order event received: {} = {}", BUYER_NAME, "state: PAID");
    }

    @Test
    public void shouldConsumeOrderCancelledEvent() {
        prepareKafkaConsumer(new OrderFinishedDto("CANCELLED"));

        logOrderEventConsumer.log();

        verify(backLogger).info("Order event received: {} = {}", BUYER_NAME, "state: CANCELLED");
    }

    private void prepareKafkaConsumer(Serializable eventDto) {
        when(kafkaConsumer.poll(any())).thenReturn(new ConsumerRecords<>(Map.of(
                new TopicPartition(orderEventsTopic, 0), List.of(
                        new ConsumerRecord<>(orderEventsTopic, 0, 0, BUYER_NAME, eventDto)
                )
        )));
    }
}
