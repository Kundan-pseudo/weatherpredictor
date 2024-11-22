package com.example.weatherpredictor.controller.implementation;

import com.example.weatherpredictor.controller.WeatherController;
import com.example.weatherpredictor.model.WeatherResponse;
import com.example.weatherpredictor.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@Validated
@Slf4j
@RequiredArgsConstructor
public class WeatherControllerImpl implements WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeatherForecast(String city) {
        log.debug("WeatherController::getWeatherForecast");
        WeatherResponse response = weatherService.getWeatherForecast(city);
        return ResponseEntity.ok(response);
    }
}