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
public class Address {

    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;

    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "post_code", nullable = false, length = 20)
    private String postCode;

    @Builder
    public Address(Integer houseNumber, String street, String city, String country, String postCode) {
        if (houseNumber == null || houseNumber <= 0) {
            throw new IllegalArgumentException("House number must be positive");
        }
        this.houseNumber = houseNumber;
        this.street = validateNotBlank(street, "Street");
        this.city = validateNotBlank(city, "City");
        this.country = validateNotBlank(country, "Country");
        this.postCode = validateNotBlank(postCode, "Post code");
    }

    private String validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(country, address.country) &&
                Objects.equals(postCode, address.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, street, city, country, postCode);
    }
}
