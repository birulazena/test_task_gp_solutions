package com.github.birulazena.hotel_app_test_task.hotel.controller;

import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelDetailsResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.CreateHotelRequestDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelShortResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class HotelController implements HotelApi {

    private final HotelService hotelService;

    @Override
    public ResponseEntity<List<HotelShortResponseDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @Override
    public ResponseEntity<HotelDetailsResponseDto> getHotelById(Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @Override
    public ResponseEntity<List<HotelShortResponseDto>> searchHotels(HotelSearchFilter filter) {
        return ResponseEntity.ok(hotelService.searchHotels(filter));
    }

    @Override
    public ResponseEntity<HotelShortResponseDto> createHotel(CreateHotelRequestDto createDto) {
        HotelShortResponseDto createdHotel = hotelService.createHotel(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @Override
    public ResponseEntity<Void> addAmenities(Long id, List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }
}
