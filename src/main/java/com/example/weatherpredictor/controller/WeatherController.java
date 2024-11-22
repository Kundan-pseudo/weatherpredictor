package com.example.weatherpredictor.controller;

import com.example.weatherpredictor.model.WeatherResponse;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;

public interface WeatherController {
    ResponseEntity<WeatherResponse> getWeatherForecast(@Pattern(regexp = "^[a-zA-Z ]+$", message = "City name must only contain letters and spaces") String city);
}

