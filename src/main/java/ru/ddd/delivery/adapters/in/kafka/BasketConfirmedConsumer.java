package ru.ddd.delivery.adapters.in.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import queues.basket.events.BasketEventsProto;
import ru.ddd.delivery.core.application.commands.CreateOrderCommand;
import ru.ddd.delivery.core.application.commands.CreateOrderCommandHandler;

@Service
@RequiredArgsConstructor
public class BasketConfirmedConsumer {
    private final CreateOrderCommandHandler createOrderCommandHandler;

    @KafkaListener(topics = "${app.kafka.basket-events-topic}")
    public void listen(byte[] message) {
        try {
            var event = BasketEventsProto.BasketConfirmedIntegrationEvent.parseFrom(message);

            var createCommandResult = CreateOrderCommand.create(
                UUID.fromString(event.getBasketId()),
                event.getAddress().getStreet(),
                event.getVolume()
            );
            if (createCommandResult.isFailure()) {
                throw new RuntimeException("Invalid command: " + createCommandResult.getError());
            }
            var command = createCommandResult.getValue();

            var handleCommandResult = this.createOrderCommandHandler.handle(command);
            if (handleCommandResult.isFailure()) {
                throw new RuntimeException("Failed to handle command: " + handleCommandResult.getError());
            }

        } catch (com.google.protobuf.InvalidProtocolBufferException ex) {
            throw new RuntimeException("Failed to parse protobuf message", ex);
        }
    }
}
