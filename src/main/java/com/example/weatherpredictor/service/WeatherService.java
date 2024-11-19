package com.example.weatherpredictor.service;

import com.example.weatherpredictor.model.WeatherResponse;

public interface WeatherService {
    /**
     * Fetches the weather forecast for the given city.
     *
     * @param city The name of the city.
     * @return WeatherResponse containing the forecast data.
     */
    WeatherResponse getWeatherForecast(String city);
}

