package com.github.birulazena.hotel_app_test_task.unit.hotel.entity;

import com.github.birulazena.hotel_app_test_task.hotel.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for Address")
class AddressTest {

    @Test
    @DisplayName("Successful address creation with all fields and trimming")
    void create_Success() {
        Address address = Address.builder()
                .houseNumber(9)
                .street("  Pobediteley Avenue  ")
                .city("  Minsk  ")
                .country("  Belarus  ")
                .postCode("  220004  ")
                .build();

        assertThat(address.getHouseNumber()).isEqualTo(9);
        assertThat(address.getStreet()).isEqualTo("Pobediteley Avenue");
        assertThat(address.getCity()).isEqualTo("Minsk");
        assertThat(address.getCountry()).isEqualTo("Belarus");
        assertThat(address.getPostCode()).isEqualTo("220004");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Exception when house number is less than or equal to 0")
    void create_InvalidHouseNumber_ThrowsException(int invalidNumber) {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(invalidNumber)
                .street("Street")
                .city("City")
                .country("Country")
                .postCode("12345")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("House number must be positive");
    }

    @Test
    @DisplayName("Exception when house number is null")
    void create_NullHouseNumber_ThrowsException() {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(null)
                .street("Street")
                .city("City")
                .country("Country")
                .postCode("12345")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("House number must be positive");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when street name is blank")
    void create_BlankStreet_ThrowsException(String invalidStreet) {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(1)
                .street(invalidStreet)
                .city("City")
                .country("Country")
                .postCode("12345")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Street must not be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when city is blank")
    void create_BlankCity_ThrowsException(String invalidCity) {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(1)
                .street("Street")
                .city(invalidCity)
                .country("Country")
                .postCode("12345")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City must not be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when country is blank")
    void create_BlankCountry_ThrowsException(String invalidCountry) {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(1)
                .street("Street")
                .city("City")
                .country(invalidCountry)
                .postCode("12345")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country must not be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when postal code is blank")
    void create_BlankPostCode_ThrowsException(String invalidPostCode) {
        assertThatThrownBy(() -> Address.builder()
                .houseNumber(1)
                .street("Street")
                .city("City")
                .country("Country")
                .postCode(invalidPostCode)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Post code must not be blank");
    }

    @Test
    @DisplayName("Correct behavior of equals and hashCode")
    void equalsAndHashCode() {
        Address a1 = Address.builder().houseNumber(9).street("Street").city("Minsk").country("Belarus").postCode("220004").build();
        Address a2 = Address.builder().houseNumber(9).street("Street").city("Minsk").country("Belarus").postCode("220004").build();
        Address a3 = Address.builder().houseNumber(10).street("Street").city("Minsk").country("Belarus").postCode("220004").build();

        assertThat(a1)
                .isEqualTo(a2)
                .hasSameHashCodeAs(a2)
                .isNotEqualTo(a3);
    }
}
