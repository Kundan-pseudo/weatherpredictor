package com.example.weatherpredictor;

import com.example.weatherpredictor.controller.WeatherController;
import com.example.weatherpredictor.model.WeatherResponse;
import com.example.weatherpredictor.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private WeatherResponse weatherResponse;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWeatherForecast_ShouldReturn200Ok() throws Exception {
        when(weatherService.getWeatherForecast("ranchi")).thenReturn(weatherResponse);

        // When & Then: Perform a GET request to /api/weather with city parameter and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather")
                        .param("city", "ranchi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Verify HTTP status 200 OK
                //.andExpect(MockMvcResultMatchers.jsonPath("$.weather").value("Sunny"))  // Check "weather" field
                //.andExpect(MockMvcResultMatchers.jsonPath("$.temperature").value(25))  // Check "temperature" field
                //.andExpect(MockMvcResultMatchers.jsonPath("$.humidity").value(30));  // Check "humidity" field
    }

    @Test
    void getWeatherForecast_ShouldReturn500() throws Exception {

        when(weatherService.getWeatherForecast("ranchi")).thenThrow(new RuntimeException("Internal Server Runtime Error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather")
                        .param("city", "ranchi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}
