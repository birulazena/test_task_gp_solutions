package com.github.birulazena.hotel_app_test_task.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API error response structure")
public record ErrorResponseDto(
        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "HTTP status error name", example = "Not Found")
        String error,

        @Schema(description = "Detailed error message", example = "Hotel with id 1 not found")
        String message,

        @Schema(description = "Requested request path", example = "/property-view/hotels/1")
        String path,

        @Schema(description = "Timestamp when the error occurred", example = "2026-10-30T12:00:00")
        LocalDateTime timestamp
) {}