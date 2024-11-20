package com.example.weatherpredictor.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.weatherpredictor.model.WeatherResponse;
import com.example.weatherpredictor.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@RestController
@RequestMapping("/api/weather")
@Validated
@Slf4j
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeatherForecast(@RequestParam @Pattern(regexp = "^[a-zA-Z ]+$", message = "City name must only contain letters and spaces") String city) {
        log.debug("WeatherController::getWeatherForecast");
        WeatherResponse response = weatherService.getWeatherForecast(city);
        return ResponseEntity.ok(response);
    }
}