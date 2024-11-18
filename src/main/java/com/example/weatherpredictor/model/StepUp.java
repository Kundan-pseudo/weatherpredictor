package com.example.weatherpredictor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepUp {
    private String time;
    private String icon;
    private Double temp;
    private Integer humidity;
}
