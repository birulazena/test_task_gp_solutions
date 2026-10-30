package com.github.birulazena.hotel_app_test_task.unit.hotel.mapper;

import com.github.birulazena.hotel_app_test_task.hotel.dto.*;
import com.github.birulazena.hotel_app_test_task.hotel.entity.*;
import com.github.birulazena.hotel_app_test_task.hotel.mapper.HotelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for HotelMapper")
class HotelMapperTest {

    private final HotelMapper mapper = Mappers.getMapper(HotelMapper.class);

    private Address validAddress;
    private Contacts validContacts;
    private ArrivalTime validArrivalTime;
    private Hotel validHotel;

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

        validHotel = Hotel.builder()
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .description("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
                .address(validAddress)
                .contacts(validContacts)
                .arrivalTime(validArrivalTime)
                .amenities(Set.of(new Amenity("Free WiFi"), new Amenity("Free parking")))
                .build();
        validHotel.setId(1L);
    }

    @Test
    @DisplayName("formatAddress should format Address into single string according to requirements")
    void formatAddress_Success() {
        String formatted = mapper.formatAddress(validAddress);

        assertThat(formatted).isEqualTo("9 Pobediteley Avenue, Minsk, 220004, Belarus");
    }

    @Test
    @DisplayName("formatAddress should return null when address is null")
    void formatAddress_Null() {
        assertThat(mapper.formatAddress(null)).isNull();
    }

    @Test
    @DisplayName("mapAmenityToString should return amenity name or null")
    void mapAmenityToString() {
        assertThat(mapper.mapAmenityToString(new Amenity("Free WiFi"))).isEqualTo("Free WiFi");
        assertThat(mapper.mapAmenityToString(null)).isNull();
    }

    @Test
    @DisplayName("mapStringToAmenity should return Amenity entity for valid string")
    void mapStringToAmenity_Success() {
        Amenity amenity = mapper.mapStringToAmenity("Free WiFi");

        assertThat(amenity).isNotNull();
        assertThat(amenity.getName()).isEqualTo("Free WiFi");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("mapStringToAmenity should return null for empty or null strings")
    void mapStringToAmenity_NullOrBlank(String input) {
        assertThat(mapper.mapStringToAmenity(input)).isNull();
    }

    @Test
    @DisplayName("toAmenitySet should map list of strings to Set of Amenity")
    void toAmenitySet() {
        List<String> amenities = List.of("Free WiFi", "Free parking");

        Set<Amenity> result = mapper.toAmenitySet(amenities);

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(new Amenity("Free WiFi"), new Amenity("Free parking"));
    }

    @Test
    @DisplayName("toShortDto should map Hotel to HotelShortResponseDto with formatted address and phone")
    void toShortDto_Success() {
        HotelShortResponseDto dto = mapper.toShortDto(validHotel);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(dto.description()).isEqualTo("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...");
        assertThat(dto.address()).isEqualTo("9 Pobediteley Avenue, Minsk, 220004, Belarus");
        assertThat(dto.phone()).isEqualTo("+375 17 309-80-00");
    }

    @Test
    @DisplayName("toShortDto should return null if hotel is null")
    void toShortDto_Null() {
        assertThat(mapper.toShortDto(null)).isNull();
    }

    @Test
    @DisplayName("toShortDtoList should map list of hotels to list of short DTOs")
    void toShortDtoList_Success() {
        List<HotelShortResponseDto> list = mapper.toShortDtoList(List.of(validHotel));

        assertThat(list).hasSize(1);
        assertThat(list.get(0).name()).isEqualTo("DoubleTree by Hilton Minsk");
    }

    @Test
    @DisplayName("toDetailsDto should map Hotel to detailed DTO with nested records and amenities list")
    void toDetailsDto_Success() {
        HotelDetailsResponseDto dto = mapper.toDetailsDto(validHotel);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(dto.brand()).isEqualTo("Hilton");
        assertThat(dto.description()).isEqualTo("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...");

        assertThat(dto.address()).isNotNull();
        assertThat(dto.address().houseNumber()).isEqualTo(9);
        assertThat(dto.address().street()).isEqualTo("Pobediteley Avenue");
        assertThat(dto.address().city()).isEqualTo("Minsk");
        assertThat(dto.address().country()).isEqualTo("Belarus");
        assertThat(dto.address().postCode()).isEqualTo("220004");

        assertThat(dto.contacts()).isNotNull();
        assertThat(dto.contacts().phone()).isEqualTo("+375 17 309-80-00");
        assertThat(dto.contacts().email()).isEqualTo("doubletreeminsk.info@hilton.com");

        assertThat(dto.arrivalTime()).isNotNull();
        assertThat(dto.arrivalTime().checkIn()).isEqualTo(LocalTime.of(14, 0));
        assertThat(dto.arrivalTime().checkOut()).isEqualTo(LocalTime.of(12, 0));

        assertThat(dto.amenities()).containsExactlyInAnyOrder("Free WiFi", "Free parking");
    }

    @Test
    @DisplayName("toDetailsDto should return null if hotel is null")
    void toDetailsDto_Null() {
        assertThat(mapper.toDetailsDto(null)).isNull();
    }

    @Test
    @DisplayName("toEntity should map CreateHotelRequestDto to Hotel entity")
    void toEntity_Success() {
        CreateHotelRequestDto requestDto = new CreateHotelRequestDto(
                "DoubleTree by Hilton Minsk",
                "Luxury hotel description",
                "Hilton",
                new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
                new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );

        Hotel hotel = mapper.toEntity(requestDto);

        assertThat(hotel).isNotNull();
        assertThat(hotel.getName()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(hotel.getBrand()).isEqualTo("Hilton");
        assertThat(hotel.getDescription()).isEqualTo("Luxury hotel description");

        assertThat(hotel.getAddress()).isNotNull();
        assertThat(hotel.getAddress().getHouseNumber()).isEqualTo(9);
        assertThat(hotel.getAddress().getCity()).isEqualTo("Minsk");

        assertThat(hotel.getContacts()).isNotNull();
        assertThat(hotel.getContacts().getPhone()).isEqualTo("+375 17 309-80-00");

        assertThat(hotel.getArrivalTime()).isNotNull();
        assertThat(hotel.getArrivalTime().getCheckIn()).isEqualTo(LocalTime.of(14, 0));
        assertThat(hotel.getArrivalTime().getCheckOut()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("toEntity should return null if request DTO is null")
    void toEntity_Null() {
        assertThat(mapper.toEntity(null)).isNull();
    }
}
