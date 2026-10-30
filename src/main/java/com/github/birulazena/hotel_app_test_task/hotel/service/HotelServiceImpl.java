package com.github.birulazena.hotel_app_test_task.hotel.service;

import com.github.birulazena.hotel_app_test_task.hotel.dto.CreateHotelRequestDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelDetailsResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelShortResponseDto;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Amenity;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import com.github.birulazena.hotel_app_test_task.hotel.exception.HotelNotFoundException;
import com.github.birulazena.hotel_app_test_task.hotel.mapper.HotelMapper;
import com.github.birulazena.hotel_app_test_task.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelShortResponseDto> getAllHotels() {
        log.info("Fetching all hotels");
        List<Hotel> hotels = hotelRepository.findAll();
        return hotelMapper.toShortDtoList(hotels);
    }

    @Override
    public HotelDetailsResponseDto getHotelById(Long id) {
        log.info("Fetching hotel details for id: {}", id);
        return hotelRepository.findById(id)
                .map(hotelMapper::toDetailsDto)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HotelShortResponseDto createHotel(CreateHotelRequestDto createDto) {
        log.info("Creating a new hotel: {}", createDto.name());
        Hotel hotel = hotelMapper.toEntity(createDto);
        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully with id: {}", savedHotel.getId());
        return hotelMapper.toShortDto(savedHotel);
    }

    @Override
    public List<HotelShortResponseDto> searchHotels(HotelSearchFilter filter) {
        log.info("Searching hotels with filter: {}", filter);
        List<Hotel> hotels = hotelRepository.search(filter);
        return hotelMapper.toShortDtoList(hotels);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addAmenities(Long id, List<String> amenities) {
        log.info("Adding amenities to hotel id: {}", id);

        if (amenities == null || amenities.isEmpty()) {
            throw new IllegalArgumentException("Amenities list cannot be empty");
        }

        boolean hasBlankValues = amenities.stream().anyMatch(a -> a == null || a.isBlank());
        if (hasBlankValues) {
            throw new IllegalArgumentException("Amenity names cannot be blank");
        }

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

        Set<Amenity> amenityEntities = hotelMapper.toAmenitySet(amenities);
        hotel.addAmenities(amenityEntities);
        hotelRepository.save(hotel);

        log.info("Added {} amenities to hotel id: {}", amenityEntities.size(), id);
    }
}