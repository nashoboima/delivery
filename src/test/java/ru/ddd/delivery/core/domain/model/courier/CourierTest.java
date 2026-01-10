package ru.ddd.delivery.core.domain.model.courier;

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
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.domain.model.order.OrderStatus;

public class CourierTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(new Object[] { null, 2, Location.create(5, 5).getValue() }, new Object[] { "K1", 2, null });
    }

    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        String name = "K1";
        int speed = 2;
        Location location = Location.create(1, 1).getValue();
        
        // Act
        var result = Courier.create(name, speed, location);

        // Assert
        var courier = result.getValue();
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(courier.getName()).isEqualTo(name),
            () -> assertThat(courier.getSpeed()).isEqualTo(speed),
            () -> assertThat(courier.getLocation()).isEqualTo(location)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void shouldReturnErrorWhenVolumeIsNotCorrectOnCreated(int speed) {
        // Arrange
        String name = "K1";
        Location location = Location.create(1, 1).getValue();

        // Act
        var result = Courier.create(name, speed, location);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidItemParams")
    void shouldThrowExceptionWhenParamsAreNullOnCreated(String name, int speed, Location location) {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class,
                () -> Courier.create(name, speed, location)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldAddStoragePlace() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 15).getValue();
        
        // Act
        var result = courier.addStoragePlace( "rack", 20);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(courier.canTakeOrder(order).isSuccess()).isTrue()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void shouldNotAddStoragePlaceWhenVolumeIsNotCorrect(int volume) {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        
        // Act
        var result = courier.addStoragePlace( "rack", volume);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldBeAbleToTakeOrderWhenSuitableStoragePlaceExist() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        
        // Act
        var result = courier.canTakeOrder(order);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue()).isTrue()
        );
    }

    @Test
    void shouldNotBeAbleToTakeOrderWhenAllStoragePlacesOccupied() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        courier.takeOrder(order1);
        courier.takeOrder(order2);
        var order3 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        
        // Act
        var result = courier.canTakeOrder(order3);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue()).isFalse()
        );
    }

    @Test
    void shouldNotBeAbleToTakeOrderWhenAllStoragePlacesVolumeIsExceeded() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 30).getValue();
        courier.takeOrder(order1);
        
        // Act
        var result = courier.canTakeOrder(order2);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue()).isFalse()
        );
    }

    @Test
    void shouldTakeOrderWhenSuitableStoragePlaceExist() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        
        // Act
        var result = courier.takeOrder(order);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.ASSIGNED)
        );
    }

    @Test
    void shouldNotTakeOrderWhenAllStoragePlacesOccupied() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        courier.takeOrder(order1);
        courier.takeOrder(order2);
        var order3 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 20).getValue();
        
        // Act
        var result = courier.takeOrder(order3);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldNotTakeOrderWhenAllStoragePlacesVolumeIsExceeded() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        courier.addStoragePlace( "rack", 20);
        var order1 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        var order2 = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 30).getValue();
        courier.takeOrder(order1);
        
        // Act
        var result = courier.takeOrder(order2);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldCompleteOrderWhenOrderAssigned() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        courier.takeOrder(order);
        
        // Act
        var result = courier.completeOrder(order);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED),
            () -> assertThat(courier.canTakeOrder(order).getValue()).isTrue()
        );
    }

    @Test
    void shouldNotCompleteOrderWhenOrderNotAssigned() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(5, 5).getValue(), 10).getValue();
        
        // Act
        var result = courier.completeOrder(order);

        // Assert
        assertAll(
            () -> assertThat(result.isFailure()).isTrue(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldCalculateTimeToLocation() {
        // Arrange
        var courier = Courier.create("k1", 2, Location.create(1, 1).getValue()).getValue();
        var targetLocation = Location.create(5, 5).getValue();
        
        // Act
        var result = courier.calculateTimeToLocation(targetLocation);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue()).isEqualByComparingTo(Double.valueOf(4.0))
        );
    }
}
