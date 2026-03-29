package ru.ddd.delivery.core.domain.model.order.events;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.libs.ddd.DomainEvent;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
public class OrderCompletedDomainEvent extends DomainEvent {
    private final UUID orderId;
    private final UUID courierId;

    public OrderCompletedDomainEvent(Order order) {
        super(order);
        this.orderId = order.getId();
        this.courierId = order.getCourierId();
    }
}
