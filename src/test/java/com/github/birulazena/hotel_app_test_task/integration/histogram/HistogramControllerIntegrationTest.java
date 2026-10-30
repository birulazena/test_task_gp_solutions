package com.github.birulazena.hotel_app_test_task.integration.histogram;

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

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HistogramControllerIntegrationTest {

    private static final String CONTEXT_PATH = "/property-view";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /property-view/histogram/city")
    class CityHistogramTests {

        @Test
        @DisplayName("Should return correct hotel counts grouped by city")
        void shouldReturnCountsGroupedByCity() throws Exception {
            createHotel("Hotel Minsk 1", "Brand A", "Minsk", "Belarus");
            createHotel("Hotel Minsk 2", "Brand B", "Minsk", "Belarus");
            createHotel("Hotel Brest 1", "Brand A", "Brest", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "city")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Minsk']").value(2))
                    .andExpect(jsonPath("$['Brest']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/brand")
    class BrandHistogramTests {

        @Test
        @DisplayName("Should return correct hotel counts grouped by brand")
        void shouldReturnCountsGroupedByBrand() throws Exception {
            createHotel("Hilton City 1", "Hilton", "Minsk", "Belarus");
            createHotel("Hilton City 2", "Hilton", "Grodno", "Belarus");
            createHotel("Marriott City", "Marriott", "Minsk", "Belarus");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "brand")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Hilton']").value(2))
                    .andExpect(jsonPath("$['Marriott']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/country")
    class CountryHistogramTests {

        @Test
        @DisplayName("Should return correct hotel counts grouped by country")
        void shouldReturnCountsGroupedByCountry() throws Exception {
            createHotel("Belarusian Hotel 1", "Brand A", "Minsk", "Belarus");
            createHotel("Belarusian Hotel 2", "Brand B", "Vitebsk", "Belarus");
            createHotel("Polish Hotel", "Brand C", "Warsaw", "Poland");

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "country")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Belarus']").value(2))
                    .andExpect(jsonPath("$['Poland']").value(1));
        }
    }

    @Nested
    @DisplayName("GET /property-view/histogram/amenities")
    class AmenitiesHistogramTests {

        @Test
        @DisplayName("Should return correct hotel counts grouped by amenities")
        void shouldReturnCountsGroupedByAmenities() throws Exception {
            Long hotelId1 = createHotel("Hotel One", "Brand A", "Minsk", "Belarus");
            Long hotelId2 = createHotel("Hotel Two", "Brand B", "Minsk", "Belarus");
            Long hotelId3 = createHotel("Hotel Three", "Brand C", "Minsk", "Belarus");

            addAmenities(hotelId1, """
                    ["Free WiFi", "Free parking"]
                    """);
            addAmenities(hotelId2, """
                    ["Free WiFi", "Swimming pool"]
                    """);
            addAmenities(hotelId3, """
                    ["Free parking"]
                    """);

            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "amenities")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$['Free WiFi']").value(2))
                    .andExpect(jsonPath("$['Free parking']").value(2))
                    .andExpect(jsonPath("$['Swimming pool']").value(1));
        }
    }

    @Nested
    @DisplayName("Error handling & Edge cases")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should return 400 Bad Request when grouping parameter is unsupported")
        void shouldReturn400_whenParameterIsUnsupported() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "unsupported-param")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when grouping parameter is numeric")
        void shouldReturn400_whenParameterIsNumeric() throws Exception {
            mockMvc.perform(get(CONTEXT_PATH + "/histogram/{param}", "123")
                            .contextPath(CONTEXT_PATH))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("Should return 200 OK and empty map when no amenities exist")
        void shouldReturnEmptyMap_whenNoAmenitiesExist() throws Exception {
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
                  "description": "Histogram test description",
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
