package com.github.birulazena.hotel_app_test_task.histogram.mapper;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistogramMapper {

    default HistogramResponseDto toDto(Map<String, Long> histogramMap) {
        return HistogramResponseDto.of(histogramMap);
    }
}
