package com.github.birulazena.hotel_app_test_task.histogram.service;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;

public interface HistogramService {
    HistogramResponseDto getHistogram(String param);
}
