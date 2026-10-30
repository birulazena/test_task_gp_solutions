package com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel;

import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import com.github.birulazena.hotel_app_test_task.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Profile("!mongo")
@RequiredArgsConstructor
public class JpaHotelRepositoryAdapter implements HotelRepository {

    private final SpringDataJpaHotelRepository jpaRepository;

    @Override
    public List<Hotel> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Hotel> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Hotel save(Hotel hotel) {
        return jpaRepository.save(hotel);
    }

    @Override
    public List<Hotel> search(HotelSearchFilter filter) {
        return jpaRepository.findAll(HotelSpecifications.withFilter(filter));
    }
}
