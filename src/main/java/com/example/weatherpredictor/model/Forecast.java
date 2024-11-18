package com.example.weatherpredictor.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forecast {
    @JsonProperty("main")
    private Main main;
    @JsonProperty("weather")
    private List<Weather> weather;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("dt_txt")
    private String dt;
}
