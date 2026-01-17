package ru.ddd.delivery.core.domain.model.courier;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ru.ddd.delivery.core.domain.model.Volume;

public class StoragePlaceTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(
            new Object[] { null, Volume.create(20).getValue() },
            new Object[] { "Backpack", null });
    }

    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        
        // Act
        var result = StoragePlace.create(name, volume);

        // Assert
        var storagePlace = result.getValue();
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(storagePlace.getName()).isEqualTo(name),
            () -> assertThat(storagePlace.getTotalVolume()).isEqualTo(volume)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidItemParams")
    void shouldThrowExceptionWhenParamsAreNullOnCreated(String name, Volume volume) {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class,
                () -> StoragePlace.create(name, volume)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldReturnCanStoreWhenStoragePlaceIsNotOccupiedAndNotExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        Boolean canStore = storagePlace.canStore(Volume.create(10).getValue()).getValue();

        // Assert
        assertThat(canStore).isTrue();
    }

    @Test
    void shouldReturnCannotStoreWhenStoragePlaceIsOccupied() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        storagePlace.store(UUID.randomUUID(), Volume.create(10).getValue());
        
        // Act
        boolean canStore = storagePlace.canStore(Volume.create(10).getValue()).getValue();

        // Assert
        assertThat(canStore).isFalse();
    }

    @Test
    void shouldReturnCannotStoreWhenExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        boolean canStore = storagePlace.canStore(Volume.create(30).getValue()).getValue();

        // Assert
        assertThat(canStore).isFalse();
    }

    @Test
    void shouldStoreWhenStoragePlaceIsNotOccupiedAndNotExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), Volume.create(10).getValue());

        // Assert
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void shouldNotStoreWhenStoragePlaceIsOccupied() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        storagePlace.store(UUID.randomUUID(), Volume.create(10).getValue());
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), Volume.create(15).getValue());

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldNotStoreWhenExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        Volume volume = Volume.create(20).getValue();
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), Volume.create(30).getValue());

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }
}
