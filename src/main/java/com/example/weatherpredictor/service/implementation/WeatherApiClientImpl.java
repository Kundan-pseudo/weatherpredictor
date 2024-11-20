package com.example.weatherpredictor.service.implementation;

import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.service.WeatherApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherApiClientImpl implements WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public OpenWeatherResponse getWeatherForecast(String city) {
        log.debug("WeatherApiClientImpl::getWeatherForecast");
        String url = baseUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }
}