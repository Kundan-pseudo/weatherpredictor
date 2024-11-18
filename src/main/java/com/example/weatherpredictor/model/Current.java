package com.example.weatherpredictor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Current {
    private String date;
    private Double temp;
    private double highTemp;
    private double lowTemp;
    private String advisory;
    private double feelsLike;
    private double wind;
    private double humidity;
}
