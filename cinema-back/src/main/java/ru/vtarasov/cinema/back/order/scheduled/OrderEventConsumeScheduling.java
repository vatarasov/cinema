package ru.vtarasov.cinema.back.order.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vtarasov.cinema.back.order.consumer.LogOrderEventConsumer;

@RequiredArgsConstructor
@Service
public class OrderEventConsumeScheduling {
    private final LogOrderEventConsumer logOrderEventConsumer;

    @Scheduled(fixedRate = 1)
    public void produceOrderEvents() {
        logOrderEventConsumer.log();
    }
}
