package com.example.weatherpredictor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpClientErrorResponseBody {
    String cod;
    String message;
}
