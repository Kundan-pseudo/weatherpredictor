package com.example.weatherpredictor.service.implementation;

import com.example.weatherpredictor.model.*;
import com.example.weatherpredictor.service.WeatherApiClient;
import com.example.weatherpredictor.service.WeatherCacheManager;
import com.example.weatherpredictor.service.WeatherService;
import com.example.weatherpredictor.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherApiClient weatherApiClient;

    @Autowired
    private WeatherCacheManager weatherCacheManager;

    public WeatherResponse getWeatherForecast(String city) {
        log.debug("WeatherServiceImpl::getWeatherForecast");
        OpenWeatherResponse cachedResponse = weatherCacheManager.getFromCache(city);
        if (cachedResponse != null) {
            return Helper.processWeatherData(cachedResponse);
        }
        OpenWeatherResponse apiResponse = weatherApiClient.getWeatherForecast(city);
        weatherCacheManager.setInCache(city, apiResponse);
        return Helper.processWeatherData(apiResponse);
    }
}
