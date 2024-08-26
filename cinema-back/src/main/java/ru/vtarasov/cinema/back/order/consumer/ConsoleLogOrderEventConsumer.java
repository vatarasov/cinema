package ru.vtarasov.cinema.back.order.consumer;

import java.io.Serializable;
import java.time.Duration;

import generated.ru.vtarasov.cinema.avro.dto.OrderEventDto;
import generated.ru.vtarasov.cinema.avro.dto.OrderFinishedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsoleLogOrderEventConsumer implements LogOrderEventConsumer {
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(1);

    private static final String ORDER_EVENT_TEMPLATE = "film: %s, price: %s";
    private static final String ORDER_FINISHED_TEMPLATE = "state: %s";

    private final KafkaConsumer<String, Serializable> kafkaConsumer;

    @Override
    public void log() {
        kafkaConsumer.poll(POLL_TIMEOUT).forEach(
                record -> log.info("Order event received: {} = {}", record.key(), getStringEventValue(record.value()))
        );
        kafkaConsumer.commitSync();
    }

    private String getStringEventValue(Serializable event) {
        if (event instanceof OrderEventDto e) {
            return String.format(ORDER_EVENT_TEMPLATE, e.getFilm(), e.getPrice());
        } else if (event instanceof OrderFinishedDto e) {
            return String.format(ORDER_FINISHED_TEMPLATE, e.getState());
        } else {
            throw new RuntimeException("Couldn't find event: " + event.getClass());
        }
    }
}
