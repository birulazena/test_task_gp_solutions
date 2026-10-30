package com.github.birulazena.hotel_app_test_task.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detailed hotel information")
public record HotelDetailsResponseDto(
        @Schema(description = "Hotel ID", example = "1")
        Long id,

        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
        String name,

        @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
        String description,

        @Schema(description = "Hotel brand", example = "Hilton")
        String brand,

        @Schema(description = "Hotel address")
        AddressDto address,

        @Schema(description = "Hotel contact information")
        ContactsDto contacts,

        @Schema(description = "Check-in and check-out times")
        ArrivalTimeDto arrivalTime,

        @Schema(description = "List of amenities")
        List<String> amenities
) {}
