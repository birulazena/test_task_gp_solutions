package com.github.birulazena.hotel_app_test_task.histogram.controller;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import com.github.birulazena.hotel_app_test_task.histogram.service.HistogramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistogramController implements HistogramApi {

    private final HistogramService histogramService;

    @Override
    public ResponseEntity<HistogramResponseDto> getHistogram(String param) {
        return ResponseEntity.ok(histogramService.getHistogram(param));
    }
}
