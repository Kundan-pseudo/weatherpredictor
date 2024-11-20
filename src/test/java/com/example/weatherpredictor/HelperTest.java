package com.example.weatherpredictor;

import com.example.weatherpredictor.model.*;
import com.example.weatherpredictor.utils.Helper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperTest {

    @Test
    public void testCalculateExpiryTimeInSeconds() {
        LocalDateTime mockedNow = LocalDateTime.of(2024, 11, 19, 8, 50, 20);
        int res = Helper.calcExpiryTime(900, mockedNow);
        assertEquals(580, res, "The expiry time should be the duration until the next update.");

        mockedNow = LocalDateTime.of(2024, 11, 19, 8, 40, 10);
        res = Helper.calcExpiryTime(900, mockedNow);
        assertEquals(900, res, "The expiry time should be capped at the specified expiry duration.");
    }

    @Test
    void test_process_valid_openweatherresponse() {
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        // Mocking the response data
        List<Forecast> forecastList = new ArrayList<>();
        Forecast forecast = new Forecast();
        Main main = new Main();
        main.setTemp(25.0);
        main.setFeelsLike(23.0);
        main.setHumidity(60);
        main.setTempMax(42.0);
        main.setTempMin(20.0);
        forecast.setMain(main);
        forecast.setWind(new Wind(5.0));
        forecast.setDt("2024-11-19 19:50:10");
        forecast.setWeather(List.of(new Weather("weather description", "weather icon")));
        forecastList.add(forecast);
        forecastList.add(forecast);
        mockResponse.setList(forecastList);
        mockResponse.setCity(new City("kolkata", 123, 124));

        WeatherResponse result = Helper.processWeatherData(mockResponse);

        assertEquals("kolkata", result.getCity().getName());
        assertEquals(25.0, result.getCurrent().getTemp());
        assertEquals(23.0, result.getCurrent().getFeelsLike());
        assertEquals(60, result.getCurrent().getHumidity());
    }
}
