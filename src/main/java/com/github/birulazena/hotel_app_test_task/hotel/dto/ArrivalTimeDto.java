package com.github.birulazena.hotel_app_test_task.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Check-in and check-out times")
public record ArrivalTimeDto(
        @Schema(description = "Check-in time (HH:mm)", example = "14:00", type = "string")
        @NotNull(message = "Check-in time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @Schema(description = "Check-out time (HH:mm, optional)", example = "12:00", type = "string")
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut
) {}