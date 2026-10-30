package com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel;

import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Amenity;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

public class HotelSpecifications {

    public static Specification<Hotel> withFilter(HotelSearchFilter filter) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return Specification.where(nameEquals(filter.name()))
                .and(brandEquals(filter.brand()))
                .and(cityEquals(filter.city()))
                .and(countryEquals(filter.country()))
                .and(hasAllAmenities(filter.amenities()));
    }

    private static Specification<Hotel> nameEquals(String name) {
        return (root, query, cb) -> StringUtils.hasText(name)
                ? cb.equal(cb.lower(root.get("name")), name.trim().toLowerCase())
                : null;
    }

    private static Specification<Hotel> brandEquals(String brand) {
        return (root, query, cb) -> StringUtils.hasText(brand)
                ? cb.equal(cb.lower(root.get("brand")), brand.trim().toLowerCase())
                : null;
    }

    private static Specification<Hotel> cityEquals(String city) {
        return (root, query, cb) -> StringUtils.hasText(city)
                ? cb.equal(cb.lower(root.get("address").get("city")), city.trim().toLowerCase())
                : null;
    }

    private static Specification<Hotel> countryEquals(String country) {
        return (root, query, cb) -> StringUtils.hasText(country)
                ? cb.equal(cb.lower(root.get("address").get("country")), country.trim().toLowerCase())
                : null;
    }

    private static Specification<Hotel> hasAllAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }

            List<String> cleanAmenities = amenities.stream()
                    .filter(StringUtils::hasText)
                    .map(s -> s.trim().toLowerCase())
                    .distinct()
                    .toList();

            if (cleanAmenities.isEmpty()) {
                return null;
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hotel> subqueryRoot = subquery.correlate(root);
            Join<Hotel, Amenity> amenityJoin = subqueryRoot.join("amenities");

            subquery.select(cb.countDistinct(cb.lower(amenityJoin.get("name"))));
            subquery.where(cb.lower(amenityJoin.get("name")).in(cleanAmenities));

            return cb.equal(subquery, (long) cleanAmenities.size());
        };
    }
}