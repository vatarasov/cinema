package ru.vtarasov.cinema.front.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtarasov.cinema.front.order.model.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, String> {
}
