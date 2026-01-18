package ru.ddd.delivery.core.domain.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.domain.model.order.OrderStatus;

public class OrderDispatcherTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(
            new Object[] { null, Location.create(5, 5).getValue(), Volume.create(20).getValue() },
            new Object[] { UUID.randomUUID(), null, Volume.create(20).getValue() },
            new Object[] { UUID.randomUUID(), Location.create(5, 5).getValue(), null });
    }

    @Test
    void shouldDispatchToFastestCourier() {
        // Arrange
        var courier1 = Courier.create("k1", Speed.create(2).getValue(), Location.create(1, 1).getValue()).getValue();
        courier1.addStoragePlace( "rack", Volume.create(20).getValue());
        var courier2 = Courier.create("k2", Speed.create(3).getValue(), Location.create(1, 10).getValue()).getValue();
        courier2.addStoragePlace( "rack", Volume.create(20).getValue());
        var courier3 = Courier.create("k3", Speed.create(5).getValue(), Location.create(10, 10).getValue()).getValue();
        List<Courier> couriers = List.of(courier1, courier2, courier3);

        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(20).getValue()).getValue();
        var additionalOrder = Order.create(UUID.randomUUID(), Location.create(5, 6).getValue(), Volume.create(20).getValue()).getValue();
        OrderDispatcher orderDispatcher = new OrderDispatcherImpl();
        
        // Act
        var result = orderDispatcher.dispatch(order, couriers);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue().getId()).isEqualTo(courier2.getId()),
            () -> assertTrue(result.getValue().canTakeOrder(additionalOrder).isFailure()),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.ASSIGNED)
        );
    }

    @Test
    void shouldNotDispatchWhenNobodyCanTakeOrder() {
        // Arrange
        var courier1 = Courier.create("k1", Speed.create(2).getValue(), Location.create(1, 1).getValue()).getValue();
        var courier2 = Courier.create("k2", Speed.create(3).getValue(), Location.create(1, 10).getValue()).getValue();
        var courier3 = Courier.create("k3", Speed.create(5).getValue(), Location.create(10, 10).getValue()).getValue();
        List<Courier> couriers = List.of(courier1, courier2, courier3);

        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(20).getValue()).getValue();
        OrderDispatcher orderDispatcher = new OrderDispatcherImpl();
        
        // Act
        var result = orderDispatcher.dispatch(order, couriers);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull(),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED)
        );
    }

    @Test
    void shouldNotDispatchWhenOrderAlreadyAssigned() {
        // Arrange
        var courier1 = Courier.create("k1", Speed.create(2).getValue(), Location.create(1, 1).getValue()).getValue();
        courier1.addStoragePlace( "rack", Volume.create(20).getValue());
        var courier2 = Courier.create("k2", Speed.create(3).getValue(), Location.create(1, 10).getValue()).getValue();
        courier2.addStoragePlace( "rack", Volume.create(20).getValue());
        var courier3 = Courier.create("k3", Speed.create(5).getValue(), Location.create(10, 10).getValue()).getValue();
        List<Courier> couriers = List.of(courier1, courier2, courier3);

        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(20).getValue()).getValue();
        order.assign(courier1);
        OrderDispatcher orderDispatcher = new OrderDispatcherImpl();
        
        // Act
        var result = orderDispatcher.dispatch(order, couriers);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

}
