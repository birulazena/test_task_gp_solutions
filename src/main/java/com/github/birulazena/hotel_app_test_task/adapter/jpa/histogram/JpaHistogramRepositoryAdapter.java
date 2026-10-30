package com.github.birulazena.hotel_app_test_task.adapter.jpa.histogram;

import com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel.SpringDataJpaHotelRepository;
import com.github.birulazena.hotel_app_test_task.histogram.repository.HistogramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile("!mongo")
@RequiredArgsConstructor
public class JpaHistogramRepositoryAdapter implements HistogramRepository {

    private final SpringDataJpaHotelRepository jpaRepository;

    @Override
    public Map<String, Long> getHistogram(String param) {
        if (param == null || param.isBlank()) {
            throw new IllegalArgumentException("Histogram parameter cannot be blank");
        }

        List<HistogramProjection> projections = switch (param.trim().toLowerCase()) {
            case "brand" -> jpaRepository.countGroupedByBrand();
            case "city" -> jpaRepository.countGroupedByCity();
            case "country" -> jpaRepository.countGroupedByCountry();
            case "amenities" -> jpaRepository.countGroupedByAmenities();
            default -> throw new IllegalArgumentException("Unsupported histogram parameter: " + param);
        };

        return projections.stream()
                .filter(p -> p.getParamValue() != null)
                .collect(Collectors.toMap(
                        HistogramProjection::getParamValue,
                        HistogramProjection::getCount,
                        Long::sum
                ));
    }
}
