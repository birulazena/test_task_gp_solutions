package com.github.birulazena.hotel_app_test_task.unit.adapter.jpa.histogram;

import com.github.birulazena.hotel_app_test_task.adapter.jpa.histogram.HistogramProjection;
import com.github.birulazena.hotel_app_test_task.adapter.jpa.histogram.JpaHistogramRepositoryAdapter;
import com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel.SpringDataJpaHotelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for JpaHistogramRepositoryAdapter")
class JpaHistogramRepositoryAdapterTest {

    @Mock
    private SpringDataJpaHotelRepository jpaRepository;

    @InjectMocks
    private JpaHistogramRepositoryAdapter histogramAdapter;

    private HistogramProjection createProjection(String paramValue, Long count) {
        HistogramProjection projection = mock(HistogramProjection.class);
        when(projection.getParamValue()).thenReturn(paramValue);
        when(projection.getCount()).thenReturn(count);
        return projection;
    }

    @Test
    @DisplayName("Should return histogram by brand with lowercase conversion and trimming")
    void getHistogram_Brand_Success() {
        HistogramProjection p1 = createProjection("Hilton", 3L);
        HistogramProjection p2 = createProjection("Marriott", 5L);
        when(jpaRepository.countGroupedByBrand()).thenReturn(List.of(p1, p2));

        Map<String, Long> result = histogramAdapter.getHistogram("  BrAnD  ");

        assertThat(result)
                .hasSize(2)
                .containsEntry("Hilton", 3L)
                .containsEntry("Marriott", 5L);
        verify(jpaRepository, times(1)).countGroupedByBrand();
        verifyNoMoreInteractions(jpaRepository);
    }

    @Test
    @DisplayName("Should return histogram by city")
    void getHistogram_City_Success() {
        HistogramProjection p1 = createProjection("Minsk", 10L);
        when(jpaRepository.countGroupedByCity()).thenReturn(List.of(p1));

        Map<String, Long> result = histogramAdapter.getHistogram("city");

        assertThat(result)
                .hasSize(1)
                .containsEntry("Minsk", 10L);
        verify(jpaRepository, times(1)).countGroupedByCity();
    }

    @Test
    @DisplayName("Should return histogram by country")
    void getHistogram_Country_Success() {
        HistogramProjection p1 = createProjection("Belarus", 4L);
        when(jpaRepository.countGroupedByCountry()).thenReturn(List.of(p1));

        Map<String, Long> result = histogramAdapter.getHistogram("country");

        assertThat(result)
                .hasSize(1)
                .containsEntry("Belarus", 4L);
        verify(jpaRepository, times(1)).countGroupedByCountry();
    }

    @Test
    @DisplayName("Should return histogram by amenities")
    void getHistogram_Amenities_Success() {
        HistogramProjection p1 = createProjection("Free WiFi", 20L);
        HistogramProjection p2 = createProjection("Fitness center", 5L);
        when(jpaRepository.countGroupedByAmenities()).thenReturn(List.of(p1, p2));

        Map<String, Long> result = histogramAdapter.getHistogram("amenities");

        assertThat(result)
                .hasSize(2)
                .containsEntry("Free WiFi", 20L)
                .containsEntry("Fitness center", 5L);
        verify(jpaRepository, times(1)).countGroupedByAmenities();
    }

    @Test
    @DisplayName("Should filter null parameter values and sum duplicates")
    void getHistogram_ShouldFilterNullsAndSumDuplicates() {
        HistogramProjection p1 = createProjection("Minsk", 2L);
        HistogramProjection p2 = createProjection("Minsk", 3L);
        HistogramProjection pNull = mock(HistogramProjection.class);
        when(pNull.getParamValue()).thenReturn(null);

        when(jpaRepository.countGroupedByCity()).thenReturn(List.of(p1, p2, pNull));

        Map<String, Long> result = histogramAdapter.getHistogram("city");

        assertThat(result)
                .hasSize(1)
                .containsEntry("Minsk", 5L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Should throw IllegalArgumentException when parameter is blank or null")
    void getHistogram_BlankOrNullParam_ThrowsException(String invalidParam) {
        assertThatThrownBy(() -> histogramAdapter.getHistogram(invalidParam))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Histogram parameter cannot be blank");

        verifyNoInteractions(jpaRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"unknown", "address", "stars", "hotel"})
    @DisplayName("Should throw IllegalArgumentException when unsupported parameter is provided")
    void getHistogram_UnsupportedParam_ThrowsException(String unsupportedParam) {
        assertThatThrownBy(() -> histogramAdapter.getHistogram(unsupportedParam))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported histogram parameter: " + unsupportedParam);

        verifyNoInteractions(jpaRepository);
    }
}