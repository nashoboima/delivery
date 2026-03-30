package ru.ddd.delivery.core.ports;

import ru.ddd.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import ru.ddd.delivery.core.domain.model.order.events.OrderCreatedDomainEvent;

public interface DeliveryEventsProducer {

    public void publish(OrderCreatedDomainEvent event);

    public void publish(OrderCompletedDomainEvent event);

}
