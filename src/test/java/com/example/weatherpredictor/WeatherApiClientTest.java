package com.example.weatherpredictor;

import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.service.implementation.WeatherApiClientImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class WeatherApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherApiClientImpl weatherApiClient;

    @Test
    void testGetWeatherForecastSuccess() {
        String city = "London";
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();

        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class))).thenReturn(mockResponse);

        OpenWeatherResponse response = weatherApiClient.getWeatherForecast(city);

        assertEquals(mockResponse, response);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(OpenWeatherResponse.class));
    }
}

