package com.example.weatherpredictor;

import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.model.WeatherResponse;
import com.example.weatherpredictor.service.WeatherApiClient;
import com.example.weatherpredictor.service.WeatherCacheManager;
import com.example.weatherpredictor.service.implementation.WeatherServiceImpl;
import com.example.weatherpredictor.utils.Helper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private WeatherCacheManager weatherCacheManager;

    private static final OpenWeatherResponse cachedResponse = new OpenWeatherResponse();
    private static final OpenWeatherResponse apiResponse = cachedResponse;
    private static final WeatherResponse expectedResponse = new WeatherResponse();
    private static MockedStatic<Helper> mockedStatic;

    @BeforeAll
    static void setup() {
        mockedStatic = mockStatic(Helper.class);
        mockedStatic.when(() -> Helper.processWeatherData(apiResponse))
                .thenReturn(expectedResponse);
    }

    @AfterAll
    static void cleanSetup() {
        mockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeatherForecast_DataPresentInCache() {
        String city = "London";

        when(weatherCacheManager.getFromCache(city)).thenReturn(cachedResponse);

        WeatherResponse actualResponse = weatherService.getWeatherForecast(city);

        assertEquals(expectedResponse, actualResponse);
        verify(weatherCacheManager, times(1)).getFromCache(city);
        verify(weatherApiClient, never()).getWeatherForecast(any());
        verify(weatherCacheManager, never()).setInCache(any(), any());
    }

    @Test
    public void testGetWeatherForecast_DataNotPresentInCache() {
        String city = "Paris";

        when(weatherCacheManager.getFromCache(city)).thenReturn(null);
        when(weatherApiClient.getWeatherForecast(city)).thenReturn(apiResponse);

        WeatherResponse actualResponse = weatherService.getWeatherForecast(city);

        assertEquals(expectedResponse, actualResponse);
        verify(weatherCacheManager, times(1)).getFromCache(city);
        verify(weatherApiClient, times(1)).getWeatherForecast(city);
        verify(weatherCacheManager, times(1)).setInCache(city, apiResponse);
    }
}
