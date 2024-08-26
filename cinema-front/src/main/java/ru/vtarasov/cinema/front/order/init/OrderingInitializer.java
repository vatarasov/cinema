package ru.vtarasov.cinema.front.order.init;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtarasov.cinema.front.order.dao.BuyerRepository;
import ru.vtarasov.cinema.front.order.event.EventType;
import ru.vtarasov.cinema.front.order.model.Buyer;

@Service
public class OrderingInitializer {
    private final Set<String> buyerNames;
    private final BuyerRepository buyerRepository;
    public OrderingInitializer(@Value("${order.buyers}") String[] buyerNames,
                               BuyerRepository buyerRepository) {
        this.buyerNames = Set.of(buyerNames);
        this.buyerRepository = buyerRepository;
    }

    @Transactional
    @EventListener
    public void onContextRefreshedEvent(ContextRefreshedEvent event) {
        List<String> deprecatedBuyerNames = buyerRepository.findAll().stream()
                .map(Buyer::getName)
                .filter(Predicate.not(buyerNames::contains))
                .collect(Collectors.toList());

        buyerRepository.deleteAllByIdInBatch(deprecatedBuyerNames);

        List<Buyer> newBuyers = buyerNames.stream()
                .map(name -> {
                    Buyer buyer = new Buyer();
                    buyer.setName(name);
                    buyer.setState(EventType.ORDER_CANCELLED);
                    return buyer;
                })
                .collect(Collectors.toList());

        buyerRepository.saveAllAndFlush(newBuyers);
    }
}
