package com.github.birulazena.hotel_app_test_task.hotel.repository;

import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelRepository {
    List<Hotel> findAll();

    Optional<Hotel> findById(Long id);

    Hotel save(Hotel hotel);

    List<Hotel> search(HotelSearchFilter filter);
}
