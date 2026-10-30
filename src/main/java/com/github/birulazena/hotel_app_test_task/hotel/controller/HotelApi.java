package com.github.birulazena.hotel_app_test_task.hotel.controller;

import com.github.birulazena.hotel_app_test_task.common.dto.ErrorResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.CreateHotelRequestDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelDetailsResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelShortResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Tag(name = "Hotels", description = "Endpoints for managing hotels, searching, and hotel amenities")
public interface HotelApi {

    @Operation(
            summary = "Get all hotels",
            description = "Retrieves a brief overview list of all registered hotels."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of hotels successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelShortResponseDto.class))
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
    @GetMapping("/hotels")
    ResponseEntity<List<HotelShortResponseDto>> getAllHotels();

    @Operation(
            summary = "Get hotel by ID",
            description = "Retrieves extended and detailed information about a specific hotel by its identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotel details successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelDetailsResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplier/format",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
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
    @GetMapping("/hotels/{id}")
    ResponseEntity<HotelDetailsResponseDto> getHotelById(
            @Parameter(description = "Unique ID of the hotel", example = "1", required = true)
            @PathVariable @Positive Long id
    );

    @Operation(
            summary = "Search hotels",
            description = "Searches for hotels matching query parameters (name, brand, city, country, amenities) and returns brief information."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Matching hotels retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HotelShortResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters supplied",
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
    @GetMapping("/search")
    ResponseEntity<List<HotelShortResponseDto>> searchHotels(
            @ParameterObject @Valid HotelSearchFilter filter
    );

    @Operation(
            summary = "Create a new hotel",
            description = "Creates a new hotel record and returns brief information about the created hotel."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelShortResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed for request body",
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
    @PostMapping("/hotels")
    ResponseEntity<HotelShortResponseDto> createHotel(
            @Valid @org.springframework.web.bind.annotation.RequestBody CreateHotelRequestDto createDto
    );

    @Operation(
            summary = "Add amenities to hotel",
            description = "Appends a list of amenities to an existing hotel by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities successfully added"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or empty amenities list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
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
    @PostMapping("/hotels/{id}/amenities")
    ResponseEntity<Void> addAmenities(
            @Parameter(description = "Target hotel ID", example = "1", required = true)
            @PathVariable @Positive Long id,

            @RequestBody(
                    description = "List of amenity names to add",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(type = "string", example = "Free parking"))
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody
            @NotEmpty(message = "Amenities list cannot be empty")
            List<@NotBlank(message = "Amenity name cannot be blank") String> amenities
    );
}