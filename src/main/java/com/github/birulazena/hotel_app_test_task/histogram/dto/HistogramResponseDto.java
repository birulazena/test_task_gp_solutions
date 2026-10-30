package com.github.birulazena.hotel_app_test_task.histogram.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Grouping result (histogram)")
public record HistogramResponseDto(
        @JsonValue
        @Schema(description = "Value-to-count pairs", example = "{\"Minsk\": 1, \"Moscow\": 2}")
        Map<String, Long> data
) {
    public static HistogramResponseDto of(Map<String, Long> data) {
        return new HistogramResponseDto(data != null ? data : Map.of());
    }
}
