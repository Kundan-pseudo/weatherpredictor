package com.example.weatherpredictor.service.implementation;

import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.service.WeatherCacheManager;
import com.example.weatherpredictor.utils.Helper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherCacheManagerImpl implements WeatherCacheManager {

    @Value("${spring.data.redis.timeout}")
    private int expiryInSec;


    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, String> redisTemplate;

    public OpenWeatherResponse getFromCache(String key) {
        log.debug("WeatherCacheManagerImpl::getFromCache");
        try {
            String jsonValue = redisTemplate.opsForValue().get(key);
            if (jsonValue == null)
                return null;
            return objectMapper.readValue(jsonValue, OpenWeatherResponse.class);
        } catch (Exception e) {
            log.error("Exception when trying to get cache = " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public void setInCache(String key, OpenWeatherResponse result) {
        log.debug("WeatherCacheManagerImpl::setInCache");
        try {
            String jsonValue = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(key, jsonValue, Duration.ofSeconds(Helper.calcExpiryTime(expiryInSec, LocalDateTime.now())));
        } catch (Exception e) {
            log.error("Exception when trying to set cache = " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
