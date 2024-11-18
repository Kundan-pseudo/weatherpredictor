package com.example.weatherpredictor.model;

import java.util.List;

import lombok.Data;

@Data
public class OpenWeatherResponse {
    private List<Forecast> list;
    private City city;
}