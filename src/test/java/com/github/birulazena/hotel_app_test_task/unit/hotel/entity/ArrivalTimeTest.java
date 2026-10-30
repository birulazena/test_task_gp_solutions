package com.github.birulazena.hotel_app_test_task.unit.hotel.entity;

import com.github.birulazena.hotel_app_test_task.hotel.entity.ArrivalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for ArrivalTime")
class ArrivalTimeTest {

    @Test
    @DisplayName("Successful creation with all fields")
    void create_Success() {
        LocalTime checkIn = LocalTime.of(14, 0);
        LocalTime checkOut = LocalTime.of(12, 0);

        ArrivalTime arrivalTime = ArrivalTime.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();

        assertThat(arrivalTime.getCheckIn()).isEqualTo(checkIn);
        assertThat(arrivalTime.getCheckOut()).isEqualTo(checkOut);
    }

    @Test
    @DisplayName("Successful creation without checkOut")
    void create_WithoutCheckOut_Success() {
        LocalTime checkIn = LocalTime.of(14, 0);

        ArrivalTime arrivalTime = ArrivalTime.builder()
                .checkIn(checkIn)
                .build();

        assertThat(arrivalTime.getCheckIn()).isEqualTo(checkIn);
        assertThat(arrivalTime.getCheckOut()).isNull();
    }

    @Test
    @DisplayName("Exception when checkIn is null")
    void create_NullCheckIn_ThrowsException() {
        assertThatThrownBy(() -> ArrivalTime.builder().checkIn(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Check-in time is required");
    }

    @Test
    @DisplayName("Correct behavior of equals and hashCode")
    void equalsAndHashCode() {
        ArrivalTime a1 = ArrivalTime.builder().checkIn(LocalTime.of(14, 0)).checkOut(LocalTime.of(12, 0)).build();
        ArrivalTime a2 = ArrivalTime.builder().checkIn(LocalTime.of(14, 0)).checkOut(LocalTime.of(12, 0)).build();
        ArrivalTime a3 = ArrivalTime.builder().checkIn(LocalTime.of(15, 0)).build();

        assertThat(a1)
                .isEqualTo(a2)
                .hasSameHashCodeAs(a2)
                .isNotEqualTo(a3);
    }
}
