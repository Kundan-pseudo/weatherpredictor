package com.example.weatherpredictor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDetails {
    private String date;
    private List<StepUp> steps = new ArrayList<>();
}