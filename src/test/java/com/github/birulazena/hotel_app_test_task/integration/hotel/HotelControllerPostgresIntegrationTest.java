package com.github.birulazena.hotel_app_test_task.integration.hotel;

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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class HotelControllerPostgresIntegrationTest {

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
    @DisplayName("POST /property-view/hotels on PostgreSQL")
    class CreateHotelPostgresTests {

        @Test
        @DisplayName("Should persist and return 201 Created for valid hotel payload")
        void shouldReturn201_whenPayloadIsValid() throws Exception {
            String payload = """
                    {
                      "name": "Postgres Grand Hotel",
                      "description": "High performance hotel backed by PostgreSQL",
                      "brand": "PostgresBrand",
                      "address": {
                        "houseNumber": 42,
                        "street": "Database Avenue",
                        "city": "Minsk",
                        "country": "Belarus",
                        "postCode": "220000"
                      },
                      "contacts": {
                        "phone": "+375 17 555-55-55",
                        "email": "postgres@hotel.com"
                      },
                      "arrivalTime": {
                        "checkIn": "14:00",
                        "checkOut": "12:00"
                      }
                    }
                    """;

            mockMvc.perform(post(CONTEXT_PATH + "/hotels")
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("Postgres Grand Hotel"))
                    .andExpect(jsonPath("$.address").value("42 Database Avenue, Minsk, 220000, Belarus"))
                    .andExpect(jsonPath("$.phone").value("+375 17 555-55-55"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when required name is missing")
        void shouldReturn400_whenNameIsMissing() throws Exception {
            String payload = """
                    {
                      "brand": "NamelessBrand",
                      "address": {
                        "houseNumber": 1,
                        "street": "Street",
                        "city": "Minsk",
                        "country": "Belarus",
                        "postCode": "220000"
                      },
                      "contacts": {
                        "phone": "+375 17 111-11-11",
                        "email": "nameless@hotel.com"
                      },
                      "arrivalTime": {
                        "checkIn": "14:00"
                      }
                    }
                    """;

            mockMvc.perform(post(CONTEXT_PATH + "/hotels")
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("GET /property-view/hotels on PostgreSQL")
    class GetAllHotelsPostgresTests {

        @Test
        @DisplayName("Should retrieve all persisted hotels from PostgreSQL")
        void shouldReturnAllHotels() throws Exception {
            createTestHotel("PG Hotel Alpha", "AlphaBrand", "Minsk", "Belarus");
            createTestHotel("PG Hotel Beta", "BetaBrand", "Grodno", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/hotels")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                    .andExpect(jsonPath("$[*].name", hasItems("PG Hotel Alpha", "PG Hotel Beta")));
        }
    }

    @Nested
    @DisplayName("GET /property-view/hotels/{id} on PostgreSQL")
    class GetHotelByIdPostgresTests {

        @Test
        @DisplayName("Should return hotel details from PostgreSQL when exists")
        void shouldReturnDetails_whenHotelExists() throws Exception {
            Long hotelId = createTestHotel("PG Details Hotel", "DetailsBrand", "Brest", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", hotelId)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(hotelId))
                    .andExpect(jsonPath("$.name").value("PG Details Hotel"))
                    .andExpect(jsonPath("$.address.city").value("Brest"))
                    .andExpect(jsonPath("$.contacts.email").value("info@hotel.com"));
        }

        @Test
        @DisplayName("Should return 404 Not Found from PostgreSQL when hotel does not exist")
        void shouldReturn404_whenHotelDoesNotExist() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", 888888L)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("POST /property-view/hotels/{id}/amenities on PostgreSQL")
    class AddAmenitiesPostgresTests {

        @Test
        @DisplayName("Should persist amenities and verify relational mapping in PostgreSQL")
        void shouldPersistAmenitiesInPostgres() throws Exception {
            Long hotelId = createTestHotel("PG Amenities Hotel", "BrandX", "Mogilev", "Belarus");

            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    ["Free WiFi", "Gym", "Breakfast included"]
                                    """))
                    .andExpect(status().isOk());

            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", hotelId)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amenities", hasSize(3)))
                    .andExpect(jsonPath("$.amenities", hasItems("Free WiFi", "Gym", "Breakfast included")));
        }
    }

    @Nested
    @DisplayName("GET /property-view/search on PostgreSQL")
    class SearchHotelsPostgresTests {

        @Test
        @DisplayName("Should filter hotels by city in PostgreSQL")
        void shouldFilterByCityInPostgres() throws Exception {
            createTestHotel("PG Minsk Hotel", "Brand", "Minsk", "Belarus");
            createTestHotel("PG Warsaw Hotel", "Brand", "Warsaw", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("city", "Minsk"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("PG Minsk Hotel")));
        }

        @Test
        @DisplayName("Should filter hotels by amenities in PostgreSQL")
        void shouldFilterByAmenitiesInPostgres() throws Exception {
            Long hotelId = createTestHotel("PG Spa Hotel", "SpaBrand", "Grodno", "Belarus");
            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    ["Heated pool", "Sauna"]
                                    """))
                    .andExpect(status().isOk());

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("amenities", "Heated pool"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("PG Spa Hotel")));
        }

        @Test
        @DisplayName("Should return empty list when no matches found in PostgreSQL")
        void shouldReturnEmptyList_whenNoMatches() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("brand", "UnknownPostgresBrand999"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }

    private Long createTestHotel(String name, String brand, String city, String country) throws Exception {
        String payload = """
                {
                  "name": "%s",
                  "description": "Postgres test hotel",
                  "brand": "%s",
                  "address": {
                    "houseNumber": 10,
                    "street": "Test Street",
                    "city": "%s",
                    "country": "%s",
                    "postCode": "100000"
                  },
                  "contacts": {
                    "phone": "+375 17 000-00-00",
                    "email": "info@hotel.com"
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
}
