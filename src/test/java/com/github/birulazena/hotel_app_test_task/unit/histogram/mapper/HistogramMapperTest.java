package com.github.birulazena.hotel_app_test_task.unit.histogram.mapper;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import com.github.birulazena.hotel_app_test_task.histogram.mapper.HistogramMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for HistogramMapper")
class HistogramMapperTest {

    private final HistogramMapper mapper = Mappers.getMapper(HistogramMapper.class);

    @Test
    @DisplayName("Should successfully map histogram map to HistogramResponseDto")
    void toDto_Success() {
        Map<String, Long> histogramMap = Map.of(
                "Minsk", 1L,
                "Moscow", 2L
        );

        HistogramResponseDto result = mapper.toDto(histogramMap);

        assertThat(result).isNotNull();
        assertThat(result.data())
                .hasSize(2)
                .containsEntry("Minsk", 1L)
                .containsEntry("Moscow", 2L);
    }

    @Test
    @DisplayName("Should return empty map if input map is null")
    void toDto_NullMap() {
        HistogramResponseDto result = mapper.toDto(null);

        assertThat(result).isNotNull();
        assertThat(result.data()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should return empty map if input map is empty")
    void toDto_EmptyMap() {
        HistogramResponseDto result = mapper.toDto(Map.of());

        assertThat(result).isNotNull();
        assertThat(result.data()).isEmpty();
    }
}
