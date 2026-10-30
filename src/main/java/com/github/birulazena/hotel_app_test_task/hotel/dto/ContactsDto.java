package com.github.birulazena.hotel_app_test_task.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Hotel contact information")
public record ContactsDto(
        @Schema(description = "Phone number", example = "+375 17 309-80-00")
        @NotBlank(message = "Phone number is required")
        @Size(max = 50, message = "Phone must not exceed 50 characters")
        String phone,

        @Schema(description = "Email address", example = "doubletreeminsk.info@hilton.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {}