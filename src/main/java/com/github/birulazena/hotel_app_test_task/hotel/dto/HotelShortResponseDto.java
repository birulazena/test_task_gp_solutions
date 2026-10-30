package com.github.birulazena.hotel_app_test_task.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Brief hotel information")
public record HotelShortResponseDto(
        @Schema(description = "Hotel ID", example = "1")
        Long id,

        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
        String name,

        @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
        String description,

        @Schema(description = "Address as a formatted string", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
        String address,

        @Schema(description = "Phone number", example = "+375 17 309-80-00")
        String phone
) {}
