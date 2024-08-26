package ru.vtarasov.cinema.front.order.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vtarasov.cinema.front.order.producer.OrderEventProducer;

@RequiredArgsConstructor
@Service
public class OrderEventProduceScheduling {
    private final OrderEventProducer orderEventProducer;
    private volatile boolean appStarted;

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        appStarted = true;
    }

    @Scheduled(fixedRate = 500)
    @Async
    public void produceOrderEvents() {
        if (appStarted) {
            orderEventProducer.produceEvent();
        }
    }
}
