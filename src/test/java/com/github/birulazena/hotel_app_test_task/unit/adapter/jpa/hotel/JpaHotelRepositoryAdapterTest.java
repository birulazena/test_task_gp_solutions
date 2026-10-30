package com.github.birulazena.hotel_app_test_task.unit.adapter.jpa.hotel;

import com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel.JpaHotelRepositoryAdapter;
import com.github.birulazena.hotel_app_test_task.adapter.jpa.hotel.SpringDataJpaHotelRepository;
import com.github.birulazena.hotel_app_test_task.hotel.dto.HotelSearchFilter;
import com.github.birulazena.hotel_app_test_task.hotel.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for JpaHotelRepositoryAdapter")
class JpaHotelRepositoryAdapterTest {

    @Mock
    private SpringDataJpaHotelRepository jpaRepository;

    @InjectMocks
    private JpaHotelRepositoryAdapter adapter;

    private Hotel createTestHotel() {
        Hotel hotel = Hotel.builder()
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .description("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
                .address(Address.builder()
                        .houseNumber(9)
                        .street("Pobediteley Avenue")
                        .city("Minsk")
                        .country("Belarus")
                        .postCode("220004")
                        .build())
                .contacts(Contacts.builder()
                        .phone("+375 17 309-80-00")
                        .email("doubletreeminsk.info@hilton.com")
                        .build())
                .arrivalTime(ArrivalTime.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .amenities(Set.of(new Amenity("Free WiFi"), new Amenity("Free parking")))
                .build();

        hotel.setId(1L);
        return hotel;
    }

    @Test
    @DisplayName("findAll should delegate call to jpaRepository")
    void findAll_ShouldDelegate() {
        Hotel hotel = createTestHotel();
        when(jpaRepository.findAll()).thenReturn(List.of(hotel));

        List<Hotel> result = adapter.findAll();

        assertThat(result)
                .hasSize(1)
                .containsExactly(hotel);
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should delegate call to jpaRepository")
    void findById_ShouldDelegate() {
        Hotel hotel = createTestHotel();
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Optional<Hotel> result = adapter.findById(1L);

        assertThat(result)
                .isPresent()
                .contains(hotel);
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save should delegate call to jpaRepository")
    void save_ShouldDelegate() {
        Hotel hotel = createTestHotel();
        when(jpaRepository.save(hotel)).thenReturn(hotel);

        Hotel result = adapter.save(hotel);

        assertThat(result).isSameAs(hotel);
        verify(jpaRepository, times(1)).save(hotel);
    }

    @Test
    @DisplayName("search should invoke findAll with specification")
    void search_ShouldDelegateWithSpecification() {
        Hotel hotel = createTestHotel();
        HotelSearchFilter filter = new HotelSearchFilter("DoubleTree", "Hilton", "Minsk", "Belarus", List.of("Free WiFi"));
        when(jpaRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));

        List<Hotel> result = adapter.search(filter);

        assertThat(result)
                .hasSize(1)
                .containsExactly(hotel);
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }
}