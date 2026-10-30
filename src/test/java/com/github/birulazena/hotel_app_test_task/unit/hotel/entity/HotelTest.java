package com.github.birulazena.hotel_app_test_task.unit.hotel.entity;

import com.github.birulazena.hotel_app_test_task.hotel.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for Hotel entity")
class HotelTest {

    private Address validAddress;
    private Contacts validContacts;
    private ArrivalTime validArrivalTime;

    @BeforeEach
    void setUp() {
        validAddress = Address.builder()
                .houseNumber(9)
                .street("Pobediteley Avenue")
                .city("Minsk")
                .country("Belarus")
                .postCode("220004")
                .build();

        validContacts = Contacts.builder()
                .phone("+375 17 309-80-00")
                .email("doubletreeminsk.info@hilton.com")
                .build();

        validArrivalTime = ArrivalTime.builder()
                .checkIn(LocalTime.of(14, 0))
                .checkOut(LocalTime.of(12, 0))
                .build();
    }

    @Test
    @DisplayName("Successful hotel creation with all fields")
    void create_Success() {
        Hotel hotel = Hotel.builder()
                .name("  DoubleTree by Hilton Minsk  ")
                .brand("  Hilton  ")
                .description("  Some description  ")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .amenities(Set.of(new Amenity("Free WiFi")))
                .build();

        assertThat(hotel.getName()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(hotel.getBrand()).isEqualTo("Hilton");
        assertThat(hotel.getDescription()).isEqualTo("Some description");
        assertThat(hotel.getAddress()).isEqualTo(validAddress);
        assertThat(hotel.getContacts()).isEqualTo(validContacts);
        assertThat(hotel.getArrivalTime()).isEqualTo(validArrivalTime);
        assertThat(hotel.getAmenities()).containsExactly(new Amenity("Free WiFi"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when hotel name is blank")
    void create_BlankName_ThrowsException(String invalidName) {
        assertThatThrownBy(() -> Hotel.builder()
                .name(invalidName)
                .brand("Hilton")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hotel name cannot be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Exception when brand is blank")
    void create_BlankBrand_ThrowsException(String invalidBrand) {
        assertThatThrownBy(() -> Hotel.builder()
                .name("DoubleTree")
                .brand(invalidBrand)
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand cannot be blank");
    }

    @Test
    @DisplayName("Exception when address is null")
    void create_NullAddress_ThrowsException() {
        assertThatThrownBy(() -> Hotel.builder()
                .name("DoubleTree")
                .brand("Hilton")
                .address(null)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Address is required");
    }

    @Test
    @DisplayName("Exception when contacts are null")
    void create_NullContacts_ThrowsException() {
        assertThatThrownBy(() -> Hotel.builder()
                .name("DoubleTree")
                .brand("Hilton")
                .address(validAddress)
                .contacts(null)
                .arrivalTime(validArrivalTime)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Contacts are required");
    }

    @Test
    @DisplayName("Exception when arrivalTime is null")
    void create_NullArrivalTime_ThrowsException() {
        assertThatThrownBy(() -> Hotel.builder()
                .name("DoubleTree")
                .brand("Hilton")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(null)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Arrival time is required");
    }

    @Test
    @DisplayName("Method addAmenity should add amenity and ignore null")
    void addAmenity() {
        Hotel hotel = Hotel.builder()
                .name("DoubleTree")
                .brand("Hilton")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build();

        hotel.addAmenity(new Amenity("Free parking"));
        hotel.addAmenity(null);

        assertThat(hotel.getAmenities()).containsExactly(new Amenity("Free parking"));
    }

    @Test
    @DisplayName("Method addAmenities should add collection of amenities and ignore null")
    void addAmenities() {
        Hotel hotel = Hotel.builder()
                .name("DoubleTree")
                .brand("Hilton")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build();

        hotel.addAmenities(List.of(new Amenity("Free parking"), new Amenity("Fitness center")));
        hotel.addAmenities(null);

        assertThat(hotel.getAmenities()).containsExactlyInAnyOrder(
                new Amenity("Free parking"),
                new Amenity("Fitness center")
        );
    }

    @Test
    @DisplayName("Correct behavior of JPA equals and hashCode (by ID)")
    void equalsAndHashCode() {
        Hotel hotel1 = Hotel.builder()
                .name("Hotel 1")
                .brand("Brand 1")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build();
        hotel1.setId(1L);

        Hotel hotel2 = Hotel.builder()
                .name("Hotel 2")
                .brand("Brand 2")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build();
        hotel2.setId(1L);

        Hotel hotel3 = Hotel.builder()
                .name("Hotel 3")
                .brand("Brand 3")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .build();
        hotel3.setId(2L);

        assertThat(hotel1)
                .isEqualTo(hotel2)
                .hasSameHashCodeAs(hotel2)
                .isNotEqualTo(hotel3);
    }
}
