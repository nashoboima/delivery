package ru.ddd.delivery.core.domain.model.order;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.courier.Courier;

public class OrderTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(
            new Object[] { null, Location.create(5, 5).getValue(), Volume.create(20).getValue() },
            new Object[] { UUID.randomUUID(), null, Volume.create(20).getValue() },
            new Object[] { UUID.randomUUID(), Location.create(5, 5).getValue(), null });
    }

    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        Volume volume = Volume.create(20).getValue();
        
        // Act
        var result = Order.create(uuid, location, volume);

        // Assert
        var order = result.getValue();
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(order.getId()).isEqualTo(uuid),
            () -> assertThat(order.getLocation()).isEqualTo(location),
            () -> assertThat(order.getVolume()).isEqualTo(volume),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidItemParams")
    void shouldThrowExceptionWhenParamsAreNullOnCreated(UUID orderId, Location location, Volume volume) {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class,
                () -> Order.create(orderId, location, volume)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldAssignCourierWhenInCreatedStatus() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(10).getValue()).getValue();
        var courier = Courier.create("k1", Speed.create(2).getValue(), Location.create(5, 5).getValue()).getValue();
        
        // Act
        var result = order.assign(courier);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(order.getCourierId()).isEqualTo(courier.getId()),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.ASSIGNED)
        );
    }

    @Test
    void shouldNotAssignCourierWhenNotInCreatedStatus() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(10).getValue()).getValue();
        var courier1 = Courier.create("k1", Speed.create(2).getValue(), Location.create(5, 5).getValue()).getValue();
        var courier2 = Courier.create("k2", Speed.create(2).getValue(), Location.create(6, 6).getValue()).getValue();
        order.assign(courier1);
        
        // Act
        var result = order.assign(courier2);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldCompleteWhenAssigned() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(20).getValue()).getValue();
        var courier = Courier.create("k1", Speed.create(2).getValue(), Location.create(5, 5).getValue()).getValue();
        order.assign(courier);
        
        // Act
        var result = order.complete();

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED)
        );
    }

    @Test
    void shouldNotCompleteWhenNotAssigned() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), Volume.create(20).getValue()).getValue();
        
        // Act
        var result = order.complete();

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull(),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED)
        );
    }
}
