package com.github.birulazena.hotel_app_test_task.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Hotel creation request")
public record CreateHotelRequestDto(
        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Hotel name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        String name,

        @Schema(description = "Hotel description (optional)")
        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description,

        @Schema(description = "Hotel brand", example = "Hilton", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Brand is required")
        @Size(max = 100, message = "Brand must not exceed 100 characters")
        String brand,

        @Schema(description = "Address", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Address is required")
        @Valid
        AddressDto address,

        @Schema(description = "Contact information", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Contacts are required")
        @Valid
        ContactsDto contacts,

        @Schema(description = "Check-in and check-out times", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Arrival time is required")
        @Valid
        ArrivalTimeDto arrivalTime
) {}