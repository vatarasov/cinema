package ru.vtarasov.cinema.front.order.producer;

import java.io.Serializable;
import java.util.Optional;

import generated.ru.vtarasov.cinema.avro.dto.OrderEventDto;
import generated.ru.vtarasov.cinema.avro.dto.OrderFinishedDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.vtarasov.cinema.front.DatabaseTestBase;
import ru.vtarasov.cinema.front.order.dao.BuyerRepository;
import ru.vtarasov.cinema.front.order.event.EventType;
import ru.vtarasov.cinema.front.order.model.Buyer;
import ru.vtarasov.cinema.front.util.FixedRandomService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

public class OrderEventProducerTest extends DatabaseTestBase {
    private static final String BUYER_NAME = "Vova";
    private static final String FILM = "Terminator";
    private static final int PRICE = 0;

    @Value("${order.events.topic}")
    private String orderEventsTopic;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Autowired
    private FixedRandomService randomService;

    @Autowired
    private BuyerRepository buyerRepository;

    @MockBean
    private KafkaProducer<String, Serializable> kafkaProducer;

    @BeforeEach
    public void setUp() {
        randomService.setNextBoolean(true);
        randomService.setNextIntBound(0);
        randomService.setNextIntOriginBound(0);

        clearInvocations(kafkaProducer);
    }

    @Test
    public void shouldOrderCreatedEventIfCancelled() {
        createBuyer(EventType.ORDER_CANCELLED);

        orderEventProducer.produceEvent();

        verify(kafkaProducer).send(eq(
                new ProducerRecord<>(orderEventsTopic, BUYER_NAME, new OrderEventDto(FILM, PRICE))
        ));

        Buyer changedBuyer = findBuyer();
        assertEquals(EventType.ORDER_CREATED, changedBuyer.getState());
    }

    @Test
    public void shouldOrderCreatedEventIfPaid() {
        createBuyer(EventType.ORDER_PAID);

        randomService.setNextBoolean(false);
        orderEventProducer.produceEvent();

        verify(kafkaProducer).send(eq(
                new ProducerRecord<>(orderEventsTopic, BUYER_NAME, new OrderEventDto(FILM, PRICE))
        ));

        Buyer changedBuyer = findBuyer();
        assertEquals(EventType.ORDER_CREATED, changedBuyer.getState());
    }

    @Test
    public void shouldOrderPaidEventIfCreated() {
        createBuyer(EventType.ORDER_CREATED);

        orderEventProducer.produceEvent();

        verify(kafkaProducer).send(eq(
                new ProducerRecord<>(orderEventsTopic, BUYER_NAME, new OrderFinishedDto(EventType.ORDER_PAID.name()))
        ));

        Buyer changedBuyer = findBuyer();
        assertEquals(EventType.ORDER_PAID, changedBuyer.getState());
    }

    @Test
    public void shouldOrderCancelledEventIfCreated() {
        createBuyer(EventType.ORDER_CREATED);

        randomService.setNextBoolean(false);
        orderEventProducer.produceEvent();

        verify(kafkaProducer).send(eq(
                new ProducerRecord<>(orderEventsTopic, BUYER_NAME, new OrderFinishedDto(EventType.ORDER_CANCELLED.name()))
        ));

        Buyer changedBuyer = findBuyer();
        assertEquals(EventType.ORDER_CANCELLED, changedBuyer.getState());
    }

    private Buyer createBuyer(EventType eventType) {
        Buyer buyer = new Buyer();
        buyer.setName(BUYER_NAME);
        buyer.setState(eventType);
        buyerRepository.saveAndFlush(buyer);
        return buyer;
    }

    private Buyer findBuyer() {
        Optional<Buyer> changedBuyer = buyerRepository.findById(BUYER_NAME);
        assertTrue(changedBuyer.isPresent());
        return changedBuyer.get();
    }
}
