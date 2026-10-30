package com.github.birulazena.hotel_app_test_task.histogram.controller;

import com.github.birulazena.hotel_app_test_task.common.dto.ErrorResponseDto;
import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Histograms", description = "Endpoints for statistical grouping and histogram calculation")
public interface HistogramApi {

    @Operation(
            summary = "Get hotel count histogram grouped by parameter",
            description = "Calculates the number of hotels grouped by each unique value of the specified parameter. " +
                    "Supported parameter values: 'brand', 'city', 'country', 'amenities'."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histogram successfully calculated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistogramResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid histogram grouping parameter specified",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/histogram/{param}")
    ResponseEntity<HistogramResponseDto> getHistogram(
            @Parameter(
                    description = "Parameter to group hotels by (brand, city, country, amenities)",
                    example = "city",
                    required = true,
                    schema = @Schema(allowableValues = {"brand", "city", "country", "amenities"})
            )
            @PathVariable String param
    );
}
