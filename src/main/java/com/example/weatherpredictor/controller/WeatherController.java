package com.example.weatherpredictor.controller;

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
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeatherForecast(@RequestParam String city) {
        WeatherResponse response = weatherService.getWeatherForecast(city);
        return ResponseEntity.ok(response);
    }
}