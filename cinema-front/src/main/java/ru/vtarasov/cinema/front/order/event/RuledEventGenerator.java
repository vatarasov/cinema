package ru.vtarasov.cinema.front.order.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vtarasov.cinema.front.util.RandomService;

@RequiredArgsConstructor
@Service
public class RuledEventGenerator implements EventGenerator {
    private final RandomService randomService;

    @Override
    public EventType nextEvent(EventType currentEvent) {
        return switch (currentEvent) {
            case ORDER_PAID, ORDER_CANCELLED -> EventType.ORDER_CREATED;
            case ORDER_CREATED -> nextAfterOrderCreated();
        };
    }

    private EventType nextAfterOrderCreated() {
        return randomService.nextBoolean() ? EventType.ORDER_PAID : EventType.ORDER_CANCELLED;
    }
}
