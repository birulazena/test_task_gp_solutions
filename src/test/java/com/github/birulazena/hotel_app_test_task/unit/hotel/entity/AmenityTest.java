package com.github.birulazena.hotel_app_test_task.unit.hotel.entity;

import com.github.birulazena.hotel_app_test_task.hotel.entity.Amenity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for Amenity")
class AmenityTest {

    @Test
    @DisplayName("Successful creation with whitespace trimming")
    void create_Success() {
        Amenity amenity = new Amenity("  Free WiFi  ");

        assertThat(amenity.getName()).isEqualTo("Free WiFi");
        assertThat(amenity.toString()).isEqualTo("Free WiFi");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Exception when name is blank or null")
    void create_BlankName_ThrowsException(String invalidName) {
        assertThatThrownBy(() -> new Amenity(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amenity name cannot be blank");
    }

    @Test
    @DisplayName("Exception when name length exceeds 100 characters")
    void create_NameTooLong_ThrowsException() {
        String longName = "A".repeat(101);

        assertThatThrownBy(() -> new Amenity(longName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amenity name cannot exceed 100 characters");
    }

    @Test
    @DisplayName("Correct behavior of equals and hashCode")
    void equalsAndHashCode() {
        Amenity amenity1 = new Amenity("Free parking");
        Amenity amenity2 = new Amenity("Free parking");
        Amenity amenity3 = new Amenity("Free WiFi");

        assertThat(amenity1)
                .isEqualTo(amenity2)
                .hasSameHashCodeAs(amenity2)
                .isNotEqualTo(amenity3);
    }
}
