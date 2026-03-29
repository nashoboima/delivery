package ru.ddd.delivery.core.application.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ddd.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import ru.ddd.delivery.core.ports.DeliveryEventsProducer;

@RequiredArgsConstructor
@Service
public class OrderCompletedDomainEventHandler {
    private final DeliveryEventsProducer producer;

    @EventListener
    public void handle(OrderCompletedDomainEvent event) {
        producer.publish(event);
    }
}
