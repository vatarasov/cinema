package ru.vtarasov.cinema.front.order.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vtarasov.cinema.front.DatabaseTestBase;
import ru.vtarasov.cinema.front.util.FixedRandomService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.vtarasov.cinema.front.order.event.EventType.ORDER_CANCELLED;
import static ru.vtarasov.cinema.front.order.event.EventType.ORDER_CREATED;
import static ru.vtarasov.cinema.front.order.event.EventType.ORDER_PAID;

public class EventGeneratorTest extends DatabaseTestBase {
    @Autowired
    private FixedRandomService randomService;

    @Autowired
    private EventGenerator eventGenerator;

    @Test
    public void shouldReturnPaidAfterCreatedIfRandomTrue() {
        randomService.setNextBoolean(true);
        assertEquals(ORDER_PAID, eventGenerator.nextEvent(ORDER_CREATED));
    }

    @Test
    public void shouldReturnCancelAfterCreatedIfRandomFalse() {
        randomService.setNextBoolean(false);
        assertEquals(ORDER_CANCELLED, eventGenerator.nextEvent(ORDER_CREATED));
    }
}
