package com.github.birulazena.hotel_app_test_task.unit.hotel.service;

import com.github.birulazena.hotel_app_test_task.hotel.dto.*;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Amenity;
import com.github.birulazena.hotel_app_test_task.hotel.entity.Hotel;
import com.github.birulazena.hotel_app_test_task.hotel.exception.HotelNotFoundException;
import com.github.birulazena.hotel_app_test_task.hotel.mapper.HotelMapper;
import com.github.birulazena.hotel_app_test_task.hotel.repository.HotelRepository;
import com.github.birulazena.hotel_app_test_task.hotel.service.HotelServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for HotelServiceImpl")
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    @DisplayName("getAllHotels should retrieve all hotels from repository and map to short DTO list")
    void getAllHotels_Success() {
        Hotel hotel = mock(Hotel.class);
        HotelShortResponseDto shortDto = new HotelShortResponseDto(
                1L, "DoubleTree by Hilton Minsk", "Description", "9 Pobediteley Avenue", "+375 17 309-80-00"
        );

        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDtoList(List.of(hotel))).thenReturn(List.of(shortDto));

        List<HotelShortResponseDto> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1).containsExactly(shortDto);
        verify(hotelRepository).findAll();
        verify(hotelMapper).toShortDtoList(List.of(hotel));
    }

    @Test
    @DisplayName("getHotelById should return detailed DTO when hotel exists")
    void getHotelById_Found_Success() {
        Long hotelId = 1L;
        Hotel hotel = mock(Hotel.class);
        HotelDetailsResponseDto detailsDto = new HotelDetailsResponseDto(
                hotelId, "DoubleTree by Hilton Minsk", "Description", "Hilton",
                null, null, null, List.of("Free WiFi")
        );

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(detailsDto);

        HotelDetailsResponseDto result = hotelService.getHotelById(hotelId);

        assertThat(result).isEqualTo(detailsDto);
        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toDetailsDto(hotel);
    }

    @Test
    @DisplayName("getHotelById should throw HotelNotFoundException when hotel does not exist")
    void getHotelById_NotFound_ThrowsException() {
        Long nonExistingId = 999L;
        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(nonExistingId))
                .isInstanceOf(HotelNotFoundException.class);

        verify(hotelRepository).findById(nonExistingId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("createHotel should map DTO to entity, save it and return short DTO")
    void createHotel_Success() {
        CreateHotelRequestDto createDto = new CreateHotelRequestDto(
                "DoubleTree by Hilton Minsk",
                "Description",
                "Hilton",
                new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
                new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );

        Hotel unsavedHotel = mock(Hotel.class);
        Hotel savedHotel = mock(Hotel.class);
        when(savedHotel.getId()).thenReturn(1L);

        HotelShortResponseDto shortDto = new HotelShortResponseDto(
                1L, "DoubleTree by Hilton Minsk", "Description", "Address", "+375 17 309-80-00"
        );

        when(hotelMapper.toEntity(createDto)).thenReturn(unsavedHotel);
        when(hotelRepository.save(unsavedHotel)).thenReturn(savedHotel);
        when(hotelMapper.toShortDto(savedHotel)).thenReturn(shortDto);

        HotelShortResponseDto result = hotelService.createHotel(createDto);

        assertThat(result).isEqualTo(shortDto);
        verify(hotelMapper).toEntity(createDto);
        verify(hotelRepository).save(unsavedHotel);
        verify(hotelMapper).toShortDto(savedHotel);
    }

    @Test
    @DisplayName("searchHotels should delegate search to repository and map results")
    void searchHotels_Success() {
        HotelSearchFilter filter = new HotelSearchFilter("DoubleTree", null, "Minsk", null, null);
        Hotel hotel = mock(Hotel.class);
        HotelShortResponseDto shortDto = new HotelShortResponseDto(
                1L, "DoubleTree by Hilton Minsk", "Description", "Address", "+375 17 309-80-00"
        );

        when(hotelRepository.search(filter)).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDtoList(List.of(hotel))).thenReturn(List.of(shortDto));

        List<HotelShortResponseDto> result = hotelService.searchHotels(filter);

        assertThat(result).hasSize(1).containsExactly(shortDto);
        verify(hotelRepository).search(filter);
        verify(hotelMapper).toShortDtoList(List.of(hotel));
    }

    @Test
    @DisplayName("addAmenities should add amenities and save hotel when hotel exists and list is not empty")
    void addAmenities_Success() {
        Long hotelId = 1L;
        List<String> amenities = List.of("Free WiFi", "Free parking");
        Set<Amenity> amenityEntities = Set.of(new Amenity("Free WiFi"), new Amenity("Free parking"));

        Hotel hotel = mock(Hotel.class);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toAmenitySet(amenities)).thenReturn(amenityEntities);

        hotelService.addAmenities(hotelId, amenities);

        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toAmenitySet(amenities);
        verify(hotel).addAmenities(amenityEntities);
        verify(hotelRepository).save(hotel);
    }

    @Test
    @DisplayName("addAmenities should throw HotelNotFoundException when hotel is not found")
    void addAmenities_NotFound_ThrowsException() {
        Long nonExistingId = 999L;
        List<String> amenities = List.of("Free WiFi");

        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.addAmenities(nonExistingId, amenities))
                .isInstanceOf(HotelNotFoundException.class);

        verify(hotelRepository).findById(nonExistingId);
        verifyNoInteractions(hotelMapper);
        verify(hotelRepository, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("addAmenities should throw IllegalArgumentException when amenities list is null or empty")
    void addAmenities_NullOrEmptyList_ThrowsException(List<String> emptyAmenities) {
        Long hotelId = 1L;

        assertThatThrownBy(() -> hotelService.addAmenities(hotelId, emptyAmenities))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amenities list cannot be empty");

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("addAmenities should throw IllegalArgumentException when amenities list contains blank elements")
    void addAmenities_BlankElements_ThrowsException() {
        Long hotelId = 1L;
        List<String> amenitiesWithBlank = List.of("Free WiFi", "   ");

        assertThatThrownBy(() -> hotelService.addAmenities(hotelId, amenitiesWithBlank))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amenity names cannot be blank");

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }
}
