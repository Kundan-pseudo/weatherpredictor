package com.example.weatherpredictor.service;

import com.example.weatherpredictor.model.*;
import com.example.weatherpredictor.utils.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;
    @Value("${weather.api.url}")
    private String BASE_URL;
    @Value("${jedis.expiryInSec}")
    private Integer expiryInSec;

    @Autowired
    private Jedis jedis;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<OpenWeatherResponse> getPublicApiWeatherForecast(String city) {
        log.debug("WeatherService::getPublicApiWeatherForecast");
        OpenWeatherResponse cachedResponse = getFromCache(city);
        if (cachedResponse != null) return ResponseEntity.ok(cachedResponse);
        int count = Helper.getApiCallCount();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&cnt=" + count + "&units=metric";
        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
        setInCache(city, response, expiryInSec);
        return ResponseEntity.ok(response);
    }

    public OpenWeatherResponse getFromCache(String key) {
        log.debug("WeatherService::getFromCache");
        try {
            String jsonValue = jedis.get(key);
            if (jsonValue == null) return null;
            return objectMapper.readValue(jsonValue, OpenWeatherResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to OpenWeatherResponse from cache", e);
        }
    }

    public void setInCache(String key, OpenWeatherResponse result, int expiryInSeconds) {
        log.debug("WeatherService::setInCache");
        try {
            String jsonValue = objectMapper.writeValueAsString(result);
            jedis.setex(key, expiryInSeconds, jsonValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OpenWeatherResponse to JSON for cache", e);
        }
    }

    public WeatherResponse getWeatherForecast(String city) {
        log.debug("WeatherService::getWeatherForecast");
        ResponseEntity<OpenWeatherResponse> responseEntity = getPublicApiWeatherForecast(city);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            return processWeatherData(responseEntity.getBody());
        log.error("Failed to fetch weather data");
        throw new RuntimeException("Failed to fetch weather data");
    }

    public WeatherResponse processWeatherData(OpenWeatherResponse weatherData) {
        log.debug("WeatherService::processWeatherData");
        WeatherResponse weatherResponse = new WeatherResponse();
        Current current = new Current();
        Forecast currForecast = weatherData.getList().get(1);
        Main currMain = currForecast.getMain();
        current.setTemp(currMain.getTemp());
        current.setFeelsLike(currMain.getFeelsLike());
        current.setHumidity(currMain.getHumidity());
        current.setHighTemp(currMain.getTempMax());
        current.setLowTemp(currMain.getTempMin());
        current.setWind(currForecast.getWind().getSpeed());
        current.setDate(currForecast.getDt());
        current.setAdvisory(Helper.getAdvisory(currForecast, currMain));
        weatherResponse.setCurrent(current);
        weatherResponse.setCity(weatherData.getCity());

        weatherResponse.setCurrent(current);
        weatherResponse.setCity(weatherData.getCity());
        weatherResponse.setForecast(Helper.groupForecasts(weatherData.getList()));

        return weatherResponse;
    }
}
