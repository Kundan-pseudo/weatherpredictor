package com.example.weatherpredictor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WeatherResponse {
    private City city;
    private Current current;
    private List<ForecastDetails> forecast = new ArrayList<>();
}
