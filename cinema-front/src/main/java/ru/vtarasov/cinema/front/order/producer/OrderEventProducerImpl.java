package ru.vtarasov.cinema.front.order.producer;

import java.io.Serializable;
import java.util.Optional;

import generated.ru.vtarasov.cinema.avro.dto.OrderEventDto;
import generated.ru.vtarasov.cinema.avro.dto.OrderFinishedDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtarasov.cinema.front.order.dao.BuyerRepository;
import ru.vtarasov.cinema.front.order.event.EventGenerator;
import ru.vtarasov.cinema.front.order.event.EventType;
import ru.vtarasov.cinema.front.order.model.Buyer;
import ru.vtarasov.cinema.front.util.RandomService;

@Slf4j
@Service
public class OrderEventProducerImpl implements OrderEventProducer {
    private static final int MIN_PRICE_BASE = 20;
    private static final int MAX_PRICE_BASE = 40;

    private final String orderEventsTopic;
    private final String[] buyerNames;
    private final String[] films;
    private final BuyerRepository buyerRepository;
    private final KafkaProducer<String, Serializable> kafkaProducer;
    private final EventGenerator eventGenerator;
    private final RandomService randomService;

    public OrderEventProducerImpl(@Value("${order.events.topic}") String orderEventsTopic,
                                  @Value("${order.buyers}") String[] buyerNames,
                                  @Value("${order.films}") String[] films,
                                  BuyerRepository buyerRepository,
                                  KafkaProducer<String, Serializable> kafkaProducer,
                                  EventGenerator eventGenerator,
                                  RandomService randomService) {
        this.orderEventsTopic = orderEventsTopic;
        this.buyerNames = buyerNames;
        this.films = films;
        this.buyerRepository = buyerRepository;
        this.kafkaProducer = kafkaProducer;
        this.eventGenerator = eventGenerator;
        this.randomService = randomService;
    }

    @Transactional
    @Override
    public void produceEvent() {
        int randomBuyerIdx = randomService.nextInt(buyerNames.length);
        String randomBuyerName = buyerNames[randomBuyerIdx];

        Optional<Buyer> buyerOpt = buyerRepository.findById(randomBuyerName);
        if (buyerOpt.isEmpty()) {
            throw new RuntimeException("Couldn't find buyer with name: " + randomBuyerName);
        }

        Buyer buyer = buyerOpt.get();
        EventType nextState = eventGenerator.nextEvent(buyer.getState());
        ProducerRecord<String, Serializable> record = switch (nextState) {
            case ORDER_CREATED -> buildOrderCreatedEvent(randomBuyerName);
            case ORDER_PAID, ORDER_CANCELLED -> buildOrderFinishedEvent(randomBuyerName);
        };

        kafkaProducer.send(record);
        kafkaProducer.flush();

        buyer.setState(nextState);
        buyerRepository.saveAndFlush(buyer);

        log.info("Order event produced: {}", record);
    }

    private ProducerRecord<String, Serializable> buildOrderCreatedEvent(String buyerName) {
        int randomFilmIdx =randomService.nextInt(films.length);
        String randomFilm = films[randomFilmIdx];

        int randomPrice = randomService.nextInt(MIN_PRICE_BASE, MAX_PRICE_BASE);

        return new ProducerRecord<>(orderEventsTopic, buyerName, new OrderEventDto(randomFilm, randomPrice));
    }

    private ProducerRecord<String, Serializable> buildOrderFinishedEvent(String buyerName) {
        EventType randomFinishState = randomService.nextBoolean() ? EventType.ORDER_PAID : EventType.ORDER_CANCELLED;

        return new ProducerRecord<>(orderEventsTopic, buyerName, new OrderFinishedDto(randomFinishState.name()));
    }
}
