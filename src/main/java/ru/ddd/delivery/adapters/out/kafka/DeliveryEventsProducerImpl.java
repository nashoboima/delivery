package ru.ddd.delivery.adapters.out.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import queues.order.events.OrderEventsProto.OrderCompletedIntegrationEvent;
import queues.order.events.OrderEventsProto.OrderCreatedIntegrationEvent;
import ru.ddd.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import ru.ddd.delivery.core.domain.model.order.events.OrderCreatedDomainEvent;
import ru.ddd.delivery.core.ports.DeliveryEventsProducer;

@RequiredArgsConstructor
@Component
public class DeliveryEventsProducerImpl implements DeliveryEventsProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${app.kafka.delivery-events-topic}")
    private String topic;

    @Override
    public void publish(OrderCreatedDomainEvent event) {
        var integrationEvent = mapToProto(event);

        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEvent.toByteArray());
    }

    private OrderCreatedIntegrationEvent mapToProto(OrderCreatedDomainEvent event) {
        var integrationEvent = OrderCreatedIntegrationEvent.newBuilder().setOrderId(event.getOrderId().toString()).build();
        return integrationEvent;
    }

    @Override
    public void publish(OrderCompletedDomainEvent event) {
        var integrationEvent = mapToProto(event);

        kafkaTemplate.send(topic, event.getOrderId().toString(), integrationEvent.toByteArray());
    }

    private OrderCompletedIntegrationEvent mapToProto(OrderCompletedDomainEvent event) {
        var integrationEvent = OrderCompletedIntegrationEvent.newBuilder()
            .setOrderId(event.getOrderId().toString())
            .setCourierId(event.getCourierId().toString())
            .build();
        return integrationEvent;
    }

    
}
