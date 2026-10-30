package com.github.birulazena.hotel_app_test_task.hotel.dto;

import java.util.List;

public record HotelSearchFilter(
        String name,
        String brand,
        String city,
        String country,
        List<String> amenities
) {
}
