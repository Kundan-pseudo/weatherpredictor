package com.example.weatherpredictor.service;

import com.example.weatherpredictor.model.OpenWeatherResponse;

public interface WeatherApiClient {
    /**
     * Fetches the weather forecast for a given city from the public API.
     *
     * @param city The name of the city.
     * @return OpenWeatherResponse containing the weather data.
     */
    OpenWeatherResponse getWeatherForecast(String city);
}
