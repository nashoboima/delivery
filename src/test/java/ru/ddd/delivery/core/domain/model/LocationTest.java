package ru.ddd.delivery.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LocationTest {
    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange
        
        // Act
        var result = Location.create(1, 2);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertEquals(1, result.getValue().getX()),
            () -> assertEquals(2, result.getValue().getY())
        );
    }

    @ParameterizedTest
    @CsvSource({ "-1, 1", "1, 0"})
    void shouldReturnErrorWhenParamsAreNotCorrectOnCreated(int x, int y) {
        // Arrange

        // Act
        var result = Location.create(x, y);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }

     @Test
    public void shouldBeEqualWhenAllPropertiesIsEqual() {
        // Arrange
        var first = Location.create(3, 4).getValue();
        var second = Location.create(3, 4).getValue();

        // Act
        var result = first.equals(second);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void shouldBeNotEqualWhenOneOfPropertiesIsNotEqual() {
        // Arrange
        var first = Location.create(3, 4).getValue();
        var second = Location.create(4, 3).getValue();

        // Act
        var result = first.equals(second);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnDistanceWhenTargetIsNotNull() {
        // Arrange
        var target = Location.create(4, 9).getValue();
        var expectedDistance = 5;
        var source = Location.create(2, 6).getValue();

        // Act
        var result = source.distanceTo(target);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isTrue(),
            () -> assertThat(result.getValue()).isEqualTo(expectedDistance)
        );
    }

    @Test
    void shouldReturnErrorWhenTargetIsNull() {
        // Arrange
        var source = Location.create(2, 6).getValue();

        // Act
        var result = source.distanceTo(null);

        // Assert
        assertAll(
            () -> assertThat(result.isSuccess()).isFalse(),
            () -> assertThat(result.getError()).isNotNull()
        );
    }
}
