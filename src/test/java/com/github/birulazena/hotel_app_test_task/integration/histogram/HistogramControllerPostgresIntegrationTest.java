package com.github.birulazena.hotel_app_test_task.integration.histogram;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class HistogramControllerPostgresIntegrationTest {

    private static final String CONTEXT_PATH = "/property-view";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /property-view/histogram/city on PostgreSQL")
    class CityHistogramPostgresTests {

        @Test
        @DisplayName("Should aggregate and return hotel counts grouped by city in PostgreSQL")
        void shouldReturnCountsGroupedByCityInPostgres() throws Exception {
            createHotel("PG Hotel Minsk 1", "Brand A", "Minsk", "Belarus");
            createHotel("PG Hotel Minsk 2", "Brand B", "Minsk", "Belarus");
            createHotel("PG Hotel Grodno", "Brand A", "Grodno", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "city")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Minsk']").value(2))
                    .andExpect(jsonPath("$['Grodno']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/brand on PostgreSQL")
    class BrandHistogramPostgresTests {

        @Test
        @DisplayName("Should aggregate and return hotel counts grouped by brand in PostgreSQL")
        void shouldReturnCountsGroupedByBrandInPostgres() throws Exception {
            createHotel("PG Brand Hotel 1", "Radisson", "Minsk", "Belarus");
            createHotel("PG Brand Hotel 2", "Radisson", "Brest", "Belarus");
            createHotel("PG Brand Hotel 3", "Hilton", "Minsk", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "brand")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Radisson']").value(2))
                    .andExpect(jsonPath("$['Hilton']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/country on PostgreSQL")
    class CountryHistogramPostgresTests {

        @Test
        @DisplayName("Should aggregate and return hotel counts grouped by country in PostgreSQL")
        void shouldReturnCountsGroupedByCountryInPostgres() throws Exception {
            createHotel("PG Country Hotel 1", "Brand A", "Minsk", "Belarus");
            createHotel("PG Country Hotel 2", "Brand B", "Gomel", "Belarus");
            createHotel("PG Country Hotel 3", "Brand C", "Warsaw", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "country")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Belarus']").value(2))
                    .andExpect(jsonPath("$['Poland']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/amenities on PostgreSQL")
    class AmenitiesHistogramPostgresTests {

        @Test
        @DisplayName("Should aggregate and return hotel counts grouped by amenities via relational join in PostgreSQL")
        void shouldReturnCountsGroupedByAmenitiesInPostgres() throws Exception {
            Long hotelId1 = createHotel("PG Amenity Hotel 1", "Brand A", "Minsk", "Belarus");
            Long hotelId2 = createHotel("PG Amenity Hotel 2", "Brand B", "Minsk", "Belarus");

            addAmenities(hotelId1, """
                    ["Free WiFi", "Free parking"]
                    """);
            addAmenities(hotelId2, """
                    ["Free WiFi", "Spa"]
                    """);

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "amenities")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Free WiFi']").value(2))
                    .andExpect(jsonPath("$['Free parking']").value(1))
                    .andExpect(jsonPath("$['Spa']").value(1));
        }
    }

    @Nested
    @DisplayName("Error handling on PostgreSQL")
    class ErrorHandlingPostgresTests {

        @Test
        @DisplayName("Should return 400 Bad Request when grouping parameter is invalid")
        void shouldReturn400_whenParameterIsInvalid() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "invalid_grouping")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("Should return 200 OK and empty map when no data exists for grouping")
        void shouldReturnEmptyMap_whenNoDataExists() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "amenities")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(aMapWithSize(0))));
        }
    }

    private Long createHotel(String name, String brand, String city, String country) throws Exception {
        String payload = """
                {
                  "name": "%s",
                  "description": "Postgres histogram test hotel",
                  "brand": "%s",
                  "address": {
                    "houseNumber": 1,
                    "street": "Test Street",
                    "city": "%s",
                    "country": "%s",
                    "postCode": "100000"
                  },
                  "contacts": {
                    "phone": "+375 17 000-00-00",
                    "email": "histogram@hotel.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00"
                  }
                }
                """.formatted(name, brand, city, country);

        MvcResult result = mockMvc.perform(post(CONTEXT_PATH + "/hotels")
                        .contextPath(CONTEXT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }

    private void addAmenities(Long hotelId, String amenitiesJson) throws Exception {
        mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                        .contextPath(CONTEXT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson))
                .andExpect(status().isOk());
    }
}
