package com.github.birulazena.hotel_app_test_task.hotel.mapper;

import com.github.birulazena.hotel_app_test_task.hotel.dto.CreateHotelRequestDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelDetailsResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelShortResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Address;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Amenity;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HotelMapper {

    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortResponseDto toShortDto(Hotel hotel);

    List<HotelShortResponseDto> toShortDtoList(List<Hotel> hotels);

    HotelDetailsResponseDto toDetailsDto(Hotel hotel);

    Hotel toEntity(CreateHotelRequestDto dto);

    Set<Amenity> toAmenitySet(List<String> amenities);

    default String mapAmenityToString(Amenity amenity) {
        return amenity != null ? amenity.getName() : null;
    }

    default Amenity mapStringToAmenity(String name) {
        return name != null && !name.isBlank() ? Amenity.builder().name(name).build() : null;
    }

    default String formatAddress(Address address) {
        if (address == null) {
            return null;
        }
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }
}
