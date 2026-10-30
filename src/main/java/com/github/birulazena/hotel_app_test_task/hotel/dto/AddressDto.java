package com.github.birulazena.hotel_app_test_task.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Hotel address")
public record AddressDto(
        @Schema(description = "House number", example = "9")
        @NotNull(message = "House number is required")
        @Positive(message = "House number must be positive")
        Integer houseNumber,

        @Schema(description = "Street", example = "Pobediteley Avenue")
        @NotBlank(message = "Street is required")
        @Size(max = 255, message = "Street must not exceed 255 characters")
        String street,

        @Schema(description = "City", example = "Minsk")
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Schema(description = "Country", example = "Belarus")
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @Schema(description = "Postal code", example = "220004")
        @NotBlank(message = "Post code is required")
        @Size(max = 20, message = "Post code must not exceed 20 characters")
        String postCode
) {}
