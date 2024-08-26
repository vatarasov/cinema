package ru.vtarasov.cinema.front.order.init;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.vtarasov.cinema.front.DatabaseTestBase;
import ru.vtarasov.cinema.front.order.dao.BuyerRepository;
import ru.vtarasov.cinema.front.order.event.EventType;
import ru.vtarasov.cinema.front.order.model.Buyer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderingInitializerTest extends DatabaseTestBase {
    @Value("${order.buyers}")
    private Set<String> buyerNames;

    @Autowired
    private BuyerRepository buyerRepository;

    @Test
    public void shouldInitBuyers() {
        List<Buyer> initedBuyers = buyerRepository.findAll();
        assertEquals(buyerNames.size(), initedBuyers.size());

        Set<String> initedBuyerNames = initedBuyers.stream()
                .map(Buyer::getName)
                .collect(Collectors.toSet());
        assertEquals(buyerNames, initedBuyerNames);

        boolean initedStatesCancelled = initedBuyers.stream()
                .map(Buyer::getState)
                .allMatch(EventType.ORDER_CANCELLED::equals);

        assertTrue(initedStatesCancelled);
    }
}
