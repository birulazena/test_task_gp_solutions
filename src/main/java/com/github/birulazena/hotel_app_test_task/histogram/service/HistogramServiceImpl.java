package com.github.birulazena.hotel_app_test_task.histogram.service;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import com.github.birulazena.hotel_app_test_task.histogram.mapper.HistogramMapper;
import com.github.birulazena.hotel_app_test_task.histogram.repository.HistogramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class HistogramServiceImpl implements HistogramService {

    private final HistogramRepository histogramRepository;
    private final HistogramMapper histogramMapper;

    @Override
    public HistogramResponseDto getHistogram(String param) {
        log.info("Calculating histogram for param: {}", param);
        Map<String, Long> histogramData = histogramRepository.getHistogram(param);
        return histogramMapper.toDto(histogramData);
    }
}
