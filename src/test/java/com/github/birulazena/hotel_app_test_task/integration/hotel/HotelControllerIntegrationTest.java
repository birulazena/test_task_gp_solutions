package com.github.birulazena.hotel_app_test_task.integration.hotel;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
class HotelControllerIntegrationTest {

    private static final String CONTEXT_PATH = "/property-view";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("POST /property-view/hotels")
    class CreateHotelTests {

        @Test
        @DisplayName("Should return 201 Created and short DTO when request payload is valid")
        void shouldReturn201_whenPayloadIsValid() throws Exception {
            String payload = """
                    {
                      "name": "DoubleTree by Hilton Minsk",
                      "description": "Luxurious rooms in the capital city",
                      "brand": "Hilton",
                      "address": {
                        "houseNumber": 9,
                        "street": "Pobediteley Avenue",
                        "city": "Minsk",
                        "country": "Belarus",
                        "postCode": "220004"
                      },
                      "contacts": {
                        "phone": "+375 17 309-80-00",
                        "email": "doubletreeminsk.info@hilton.com"
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
                    .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                    .andExpect(jsonPath("$.description").value("Luxurious rooms in the capital city"))
                    .andExpect(jsonPath("$.address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                    .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"));
        }

        @Test
        @DisplayName("Should return 201 Created when optional fields (description, checkOut) are omitted")
        void shouldReturn201_whenOptionalFieldsAreOmitted() throws Exception {
            String payload = """
                    {
                      "name": "Minimal Hotel",
                      "brand": "Independent",
                      "address": {
                        "houseNumber": 1,
                        "street": "Main St",
                        "city": "Brest",
                        "country": "Belarus",
                        "postCode": "224000"
                      },
                      "contacts": {
                        "phone": "+375 16 200-00-00",
                        "email": "info@minimal.com"
                      },
                      "arrivalTime": {
                        "checkIn": "15:00"
                      }
                    }
                    """;

            mockMvc.perform(post(CONTEXT_PATH + "/hotels")
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("Minimal Hotel"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when required field 'name' is missing")
        void shouldReturn400_whenNameIsMissing() throws Exception {
            String payload = """
                    {
                      "brand": "Hilton",
                      "address": {
                        "houseNumber": 9,
                        "street": "Pobediteley Avenue",
                        "city": "Minsk",
                        "country": "Belarus",
                        "postCode": "220004"
                      },
                      "contacts": {
                        "phone": "+375 17 309-80-00",
                        "email": "info@hilton.com"
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

        @Test
        @DisplayName("Should return 400 Bad Request when required field 'brand' is missing")
        void shouldReturn400_whenBrandIsMissing() throws Exception {
            String payload = """
                    {
                      "name": "Nameless Brand Hotel",
                      "address": {
                        "houseNumber": 10,
                        "street": "Lenina",
                        "city": "Grodno",
                        "country": "Belarus",
                        "postCode": "230000"
                      },
                      "contacts": {
                        "phone": "+375 15 200-00-00",
                        "email": "hotel@grodno.by"
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

        @Test
        @DisplayName("Should return 400 Bad Request when email format is invalid")
        void shouldReturn400_whenEmailIsInvalid() throws Exception {
            String payload = """
                    {
                      "name": "Invalid Email Hotel",
                      "brand": "Hilton",
                      "address": {
                        "houseNumber": 10,
                        "street": "Lenina",
                        "city": "Grodno",
                        "country": "Belarus",
                        "postCode": "230000"
                      },
                      "contacts": {
                        "phone": "+375 15 200-00-00",
                        "email": "not-an-email"
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

        @Test
        @DisplayName("Should return 400 Bad Request when houseNumber is zero or negative")
        void shouldReturn400_whenHouseNumberIsInvalid() throws Exception {
            String payload = """
                    {
                      "name": "Zero House Hotel",
                      "brand": "Hilton",
                      "address": {
                        "houseNumber": 0,
                        "street": "Lenina",
                        "city": "Grodno",
                        "country": "Belarus",
                        "postCode": "230000"
                      },
                      "contacts": {
                        "phone": "+375 15 200-00-00",
                        "email": "zero@house.by"
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
    @DisplayName("GET /property-view/hotels")
    class GetAllHotelsTests {

        @Test
        @DisplayName("Should return 200 OK and list containing created hotels")
        void shouldReturn200AndListOfHotels() throws Exception {
            createTestHotel("DoubleTree by Hilton Minsk", "Hilton", "Minsk", "Belarus");
            createTestHotel("Marriott Hotel Minsk", "Marriott", "Minsk", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/hotels")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                    .andExpect(jsonPath("$[*].name", hasItems("DoubleTree by Hilton Minsk", "Marriott Hotel Minsk")));
        }
    }

    @Nested
    @DisplayName("GET /property-view/hotels/{id}")
    class GetHotelByIdTests {

        @Test
        @DisplayName("Should return 200 OK and full details when hotel exists")
        void shouldReturn200AndDetails_whenHotelExists() throws Exception {
            Long hotelId = createTestHotel("Renaissance Minsk Hotel", "Marriott", "Minsk", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", hotelId)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(hotelId))
                    .andExpect(jsonPath("$.name").value("Renaissance Minsk Hotel"))
                    .andExpect(jsonPath("$.brand").value("Marriott"))
                    .andExpect(jsonPath("$.address.city").value("Minsk"))
                    .andExpect(jsonPath("$.address.country").value("Belarus"))
                    .andExpect(jsonPath("$.contacts.email").value("info@hotel.com"))
                    .andExpect(jsonPath("$.contacts.phone").value("+375 17 000-00-00"))
                    .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                    .andExpect(jsonPath("$.amenities").isArray());
        }

        @Test
        @DisplayName("Should return 404 Not Found when hotel ID does not exist")
        void shouldReturn404_whenHotelDoesNotExist() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", 999999L)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when hotel ID has invalid format")
        void shouldReturn400_whenIdIsInvalidFormat() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", "not-a-number")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("POST /property-view/hotels/{id}/amenities")
    class AddAmenitiesTests {

        @Test
        @DisplayName("Should return 200 OK and persist amenities for the hotel")
        void shouldReturn200AndPersistAmenities_whenHotelExists() throws Exception {
            Long hotelId = createTestHotel("Hampton by Hilton Brest", "Hilton", "Brest", "Belarus");
            String amenitiesPayload = """
                    ["Free parking", "Free WiFi", "Fitness center"]
                    """;

            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(amenitiesPayload))
                    .andExpect(status().isOk());

            mockMvc.perform(get(CONTEXT_PATH + "/hotels/{id}", hotelId)
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amenities", hasSize(3)))
                    .andExpect(jsonPath("$.amenities", hasItems("Free parking", "Free WiFi", "Fitness center")));
        }

        @Test
        @DisplayName("Should return 404 Not Found when hotel does not exist")
        void shouldReturn404_whenHotelDoesNotExist() throws Exception {
            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", 999999L)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    ["Free WiFi"]
                                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when amenities list is empty")
        void shouldReturn400_whenAmenitiesListIsEmpty() throws Exception {
            Long hotelId = createTestHotel("Empty Amenities Hotel", "Brand", "Minsk", "Belarus");

            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[]"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("GET /property-view/search")
    class SearchHotelsTests {

        @Test
        @DisplayName("Should return matching hotels when filtered by name")
        void shouldFilterByName() throws Exception {
            createTestHotel("Crowne Plaza Minsk", "IHG", "Minsk", "Belarus");
            createTestHotel("Radisson Blu Warsaw", "Radisson", "Warsaw", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("name", "Crowne Plaza Minsk"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("Crowne Plaza Minsk")));
        }

        @Test
        @DisplayName("Should return matching hotels when filtered by city")
        void shouldFilterByCity() throws Exception {
            createTestHotel("Hotel Minsk One", "Hilton", "Minsk", "Belarus");
            createTestHotel("Hotel Warsaw One", "Marriott", "Warsaw", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("city", "Minsk"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("Hotel Minsk One")));
        }

        @Test
        @DisplayName("Should return matching hotels when filtered by brand")
        void shouldFilterByBrand() throws Exception {
            createTestHotel("Hilton City", "Hilton", "Minsk", "Belarus");
            createTestHotel("Marriott City", "Marriott", "Minsk", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("brand", "Hilton"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("Hilton City")));
        }

        @Test
        @DisplayName("Should return matching hotels when filtered by country")
        void shouldFilterByCountry() throws Exception {
            createTestHotel("Belarusian Hotel", "Local", "Mogilev", "Belarus");
            createTestHotel("Polish Hotel", "Local", "Krakow", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("country", "Poland"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("Polish Hotel")));
        }

        @Test
        @DisplayName("Should return matching hotels when filtered by amenities")
        void shouldFilterByAmenities() throws Exception {
            Long hotelId = createTestHotel("Spa Resort", "Luxury", "Grodno", "Belarus");
            mockMvc.perform(post(CONTEXT_PATH + "/hotels/{id}/amenities", hotelId)
                            .contextPath(CONTEXT_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    ["Free parking", "Swimming pool"]
                                    """))
                    .andExpect(status().isOk());

            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("amenities", "Swimming pool"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].name", hasItem("Spa Resort")));
        }

        @Test
        @DisplayName("Should return empty list when no hotels match search query")
        void shouldReturnEmptyList_whenNoMatchesFound() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/search")
                            .contextPath(CONTEXT_PATH)
                            .param("city", "NonExistentCity12345"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }

    private Long createTestHotel(String name, String brand, String city, String country) throws Exception {
        String payload = """
                {
                  "name": "%s",
                  "description": "Integration test description",
                  "brand": "%s",
                  "address": {
                    "houseNumber": 10,
                    "street": "Central Street",
                    "city": "%s",
                    "country": "%s",
                    "postCode": "100000"
                  },
                  "contacts": {
                    "phone": "+375 17 000-00-00",
                    "email": "info@hotel.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
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