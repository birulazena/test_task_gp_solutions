package com.github.birulazena.hotel_app_test_task.hotel.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Long id) {
        super("Hotel with id " + id + " not found");
    }
}