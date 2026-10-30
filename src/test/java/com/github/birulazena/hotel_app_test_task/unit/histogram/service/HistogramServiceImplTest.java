package com.github.birulazena.hotel_app_test_task.unit.histogram.service;

import com.github.birulazena.hotel_app_test_task.histogram.dto.HistogramResponseDto;
import com.github.birulazena.hotel_app_test_task.histogram.mapper.HistogramMapper;
import com.github.birulazena.hotel_app_test_task.histogram.repository.HistogramRepository;
import com.github.birulazena.hotel_app_test_task.histogram.service.HistogramServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for HistogramServiceImpl")
class HistogramServiceImplTest {

    @Mock
    private HistogramRepository histogramRepository;

    @Mock
    private HistogramMapper histogramMapper;

    @InjectMocks
    private HistogramServiceImpl histogramService;

    @Test
    @DisplayName("getHistogram should retrieve data from repository and return mapped DTO")
    void getHistogram_Success() {
        String param = "city";
        Map<String, Long> histogramData = Map.of("Minsk", 1L, "Moscow", 2L);
        HistogramResponseDto expectedDto = HistogramResponseDto.of(histogramData);

        when(histogramRepository.getHistogram(param)).thenReturn(histogramData);
        when(histogramMapper.toDto(histogramData)).thenReturn(expectedDto);

        HistogramResponseDto actualDto = histogramService.getHistogram(param);

        assertThat(actualDto).isEqualTo(expectedDto);
        verify(histogramRepository).getHistogram(param);
        verify(histogramMapper).toDto(histogramData);
    }

    @Test
    @DisplayName("getHistogram should propagate exception when repository throws IllegalArgumentException")
    void getHistogram_RepositoryThrowsException() {
        String param = "unsupported";
        when(histogramRepository.getHistogram(param))
                .thenThrow(new IllegalArgumentException("Unsupported histogram parameter: " + param));

        assertThatThrownBy(() -> histogramService.getHistogram(param))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported histogram parameter: unsupported");

        verify(histogramRepository).getHistogram(param);
        verifyNoInteractions(histogramMapper);
    }
}