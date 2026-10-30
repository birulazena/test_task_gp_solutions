package com.github.birulazena.hotel_app_test_task.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Amenity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Builder
    public Amenity(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Amenity name cannot be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Amenity name cannot exceed 100 characters");
        }
        this.name = name.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amenity amenity)) return false;
        return Objects.equals(name, amenity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
