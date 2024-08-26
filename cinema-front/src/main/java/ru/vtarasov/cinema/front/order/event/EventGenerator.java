package ru.vtarasov.cinema.front.order.event;

public interface EventGenerator {
    EventType nextEvent(EventType currentEvent);
}
