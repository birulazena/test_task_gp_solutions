package com.github.birulazena.hotel_app_test_task.hotel.service;

import com.github.birulazena.hotel_app_test_task.hotel.dto.CreateHotelRequestDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelDetailsResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelShortResponseDto;

import java.util.List;

public interface HotelService {

    List<HotelShortResponseDto> getAllHotels();

    HotelDetailsResponseDto getHotelById(Long id);

    HotelShortResponseDto createHotel(CreateHotelRequestDto createDto);

    List<HotelShortResponseDto> searchHotels(HotelSearchFilter filter);

    void addAmenities(Long id, List<String> amenities);
}
