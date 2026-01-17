package ru.ddd.delivery.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SpeedTest {
    @Test
    void shouldBeCorrectWhenParamsAreCorrectOnCreated() {
        // Arrange

        // Act
        var result = Speed.create(2);

        // Assert
        assertAll(
                () -> assertThat(result.isSuccess()).isTrue(),
                () -> assertEquals(2, result.getValue().getValue()));
    }

    @ParameterizedTest
    @CsvSource({ "-1", "0" })
    void shouldReturnErrorWhenValueIsNotCorrectOnCreated(int value) {
        // Arrange

        // Act
        var result = Speed.create(value);

        // Assert
        assertAll(
                () -> assertThat(result.isSuccess()).isFalse(),
                () -> assertThat(result.getError()).isNotNull());
    }

    @Test
    public void shouldBeEqualWhenValuesAreEqual() {
        // Arrange
        var first = Speed.create(2).getValue();
        var second = Speed.create(2).getValue();

        // Act
        var result = first.equals(second);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void shouldBeNotEqualWhenValuesAreNotEqual() {
        // Arrange
        var first = Speed.create(2).getValue();
        var second = Speed.create(3).getValue();

        // Act
        var result = first.equals(second);

        // Assert
        assertThat(result).isFalse();
    }

}
