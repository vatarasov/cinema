package ru.vtarasov.cinema.front.order.event;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RuledEventGenerator implements EventGenerator {
    @Override
    public EventType nextEvent(EventType currentEvent) {
        return switch (currentEvent) {
            case ORDER_PAID, ORDER_CANCELLED -> EventType.ORDER_CREATED;
            case ORDER_CREATED -> nextAfterOrderCreated();
        };
    }

    private EventType nextAfterOrderCreated() {
        return new Random().nextBoolean() ? EventType.ORDER_PAID : EventType.ORDER_CANCELLED;
    }
}
