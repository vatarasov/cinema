package ru.vtarasov.cinema.front.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import ru.vtarasov.cinema.front.order.event.EventType;

@Data
@Entity
public class Buyer {
    @Id
    private String name;
    private EventType state;
}
