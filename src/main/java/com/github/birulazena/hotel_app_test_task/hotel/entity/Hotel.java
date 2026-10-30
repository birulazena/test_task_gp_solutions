package com.github.birulazena.hotel_app_test_task.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ArrivalTime arrivalTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "hotel_amenities",
            joinColumns = @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false)
    )
    private Set<Amenity> amenities = new HashSet<>();

    @Builder
    public Hotel(String name,
                 String description,
                 String brand,
                 Address address,
                 Contacts contacts,
                 ArrivalTime arrivalTime,
                 Set<Amenity> amenities) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hotel name cannot be blank");
        }
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be blank");
        }
        this.name = name.trim();
        this.brand = brand.trim();
        this.description = description != null ? description.trim() : null;
        this.address = Objects.requireNonNull(address, "Address is required");
        this.contacts = Objects.requireNonNull(contacts, "Contacts are required");
        this.arrivalTime = Objects.requireNonNull(arrivalTime, "Arrival time is required");

        if (amenities != null) {
            this.amenities.addAll(amenities);
        }
    }

    public void addAmenity(Amenity amenity) {
        if (amenity != null) {
            this.amenities.add(amenity);
        }
    }

    public void addAmenities(Collection<Amenity> newAmenities) {
        if (newAmenities != null) {
            this.amenities.addAll(newAmenities);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel hotel)) return false;
        return id != null && id.equals(hotel.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
