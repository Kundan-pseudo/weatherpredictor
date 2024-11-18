package com.example.weatherpredictor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Main {
    @JsonProperty("temp_max")
    private double tempMax;

    @JsonProperty("temp_min")
    private double tempMin;
    @JsonProperty("temp")
    private double temp;
    @JsonProperty("feels_like")
    private Double feelsLike;
    @JsonProperty("humidity")
    private Integer humidity;
}