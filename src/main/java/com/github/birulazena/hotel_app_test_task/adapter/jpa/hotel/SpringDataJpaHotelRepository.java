package com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel;

import com.github.birulazena.hotel_app_test_task.adapter.jpa.histogram.HistogramProjection;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaHotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @EntityGraph(attributePaths = "amenities")
    Optional<Hotel> findById(Long id);

    @Query("SELECT h.brand AS paramValue, COUNT(h) AS count FROM Hotel h GROUP BY h.brand")
    List<HistogramProjection> countGroupedByBrand();

    @Query("SELECT h.address.city AS paramValue, COUNT(h) AS count FROM Hotel h GROUP BY h.address.city")
    List<HistogramProjection> countGroupedByCity();

    @Query("SELECT h.address.country AS paramValue, COUNT(h) AS count FROM Hotel h GROUP BY h.address.country")
    List<HistogramProjection> countGroupedByCountry();

    @Query("SELECT a.name AS paramValue, COUNT(DISTINCT h) AS count FROM Hotel h JOIN h.amenities a GROUP BY a.name")
    List<HistogramProjection> countGroupedByAmenities();
}