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

public class StoragePlaceTest {
    @SuppressWarnings("unused")
    static Stream<Object[]> invalidItemParams() {
        return Stream.of(new Object[] { "Backpack", 0 }, new Object[] { "Backpack", -1 });
    }

    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        
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
    void shouldReturnErrorWhenVolumeIsNotCorrectOnCreated(String name, int volume) {
        // Arrange

        // Act
        var result = StoragePlace.create(name, volume);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsNullOnCreated() {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception =  assertThrows(
                IllegalArgumentException.class,
                () -> StoragePlace.create(null, 20)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldReturnCanStoreWhenStoragePlaceIsNotOccupiedAndNotExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        Boolean canStore = storagePlace.canStore(10).getValue();

        // Assert
        assertThat(canStore).isTrue();
    }

    @Test
    void shouldReturnCannotStoreWhenStoragePlaceIsOccupied() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        storagePlace.store(UUID.randomUUID(), 10);
        
        // Act
        boolean canStore = storagePlace.canStore(10).getValue();

        // Assert
        assertThat(canStore).isFalse();
    }

    @Test
    void shouldReturnCannotStoreWhenExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        boolean canStore = storagePlace.canStore(30).getValue();

        // Assert
        assertThat(canStore).isFalse();
    }

    @Test
    void shouldStoreWhenStoragePlaceIsNotOccupiedAndNotExceededTotalVolume() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), 10);

        // Assert
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void shouldNotStoreWhenStoragePlaceIsOccupied() {
        // Arrange
        String name = "Backpack";
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        storagePlace.store(UUID.randomUUID(), 10);
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), 15);

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
        int volume = 20;
        var storagePlace = StoragePlace.create(name, volume).getValue();
        
        // Act
        var result = storagePlace.store(UUID.randomUUID(), 30);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }
}
