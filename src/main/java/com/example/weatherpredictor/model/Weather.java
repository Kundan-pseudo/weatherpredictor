package com.example.weatherpredictor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;
}
