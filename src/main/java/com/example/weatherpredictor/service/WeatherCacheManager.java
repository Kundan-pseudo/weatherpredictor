package com.example.weatherpredictor.service;

import com.example.weatherpredictor.model.OpenWeatherResponse;

public interface WeatherCacheManager {
    /**
     * Retrieves cached weather data for the given key.
     *
     * @param key The cache key (typically the city name).
     * @return OpenWeatherResponse or null if no data is cached.
     */
    OpenWeatherResponse getFromCache(String key);

    /**
     * Caches weather data with the given key.
     *
     * @param key      The cache key (typically the city name).
     * @param result   The weather data to cache.
     */
    void setInCache(String key, OpenWeatherResponse result);
}

