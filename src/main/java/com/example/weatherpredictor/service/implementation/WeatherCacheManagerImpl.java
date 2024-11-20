package com.example.weatherpredictor.service.implementation;

import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.service.WeatherCacheManager;
import com.example.weatherpredictor.utils.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class WeatherCacheManagerImpl implements WeatherCacheManager {

    @Value("${spring.data.redis.timeout}")
    private int expiryInSec;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public OpenWeatherResponse getFromCache(String key) {
        log.debug("WeatherCacheManagerImpl::getFromCache");
        try {
            String jsonValue = redisTemplate.opsForValue().get(key);
            if (jsonValue == null)
                return null;
            return objectMapper.readValue(jsonValue, OpenWeatherResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to OpenWeatherResponse from cache", e);
        }
    }

    public void setInCache(String key, OpenWeatherResponse result) {
        log.debug("WeatherCacheManagerImpl::setInCache");
        try {
            String jsonValue = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(key, jsonValue, Duration.ofSeconds(Helper.calcExpiryTime(expiryInSec, LocalDateTime.now())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OpenWeatherResponse to JSON for cache", e);
        }
    }
}
