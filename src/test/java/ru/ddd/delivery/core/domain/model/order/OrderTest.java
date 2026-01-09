package ru.ddd.delivery.core.domain.model.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.courier.Courier;

public class OrderTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(new Object[] { null, Location.create(5, 5).getValue(), 20 }, new Object[] { UUID.randomUUID(), null, 20 });
    }

    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();
        int volume = 20;
        
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
    @ValueSource(ints = { 0, -1 })
    void shouldReturnErrorWhenVolumeIsNotCorrectOnCreated(int volume) {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Location location = Location.create(5, 5).getValue();

        // Act
        var result = Order.create(uuid, location, volume);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidItemParams")
    void shouldThrowExceptionWhenParamsAreNullOnCreated(UUID orderId, Location location, int volume) {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class,
                () -> Order.create(orderId, location, volume)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldAssignCourier() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        var courier = Courier.create("k1", 2, Location.create(5, 5).getValue()).getValue();
        
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
    void shouldCompleteWhenAssigned() {
        // Arrange
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        var courier = Courier.create("k1", 2, Location.create(5, 5).getValue()).getValue();
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
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        
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
