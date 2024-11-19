package com.example.weatherpredictor.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.weatherpredictor.model.Forecast;
import com.example.weatherpredictor.model.ForecastDetails;
import com.example.weatherpredictor.model.Main;
import com.example.weatherpredictor.model.StepUp;

public class Helper {
    public static String getAdvisory(Forecast forecast, Main main) {
        if (main.getTempMax() > 40)
            return Constants.ADVICE_USE_SUNSCREEN;
        if (forecast.getWeather().stream().anyMatch(w -> w.getDescription().contains("rain")))
            return Constants.ADVICE_CARRY_UMB;
        if (forecast.getWind().getSpeed() > 10)
            return Constants.ADVICE_TOO_WINDY;
        if (forecast.getWeather().stream().anyMatch(w -> w.getDescription().contains("thunderstorm")))
            return Constants.ADVICE_TOO_THUNDER_STORM;
        return forecast.getWeather().get(0).getDescription();
    }

    public static List<ForecastDetails> groupForecasts(List<Forecast> forecastsList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, List<Forecast>> groupedForecasts = new LinkedHashMap<>();
        for (Forecast forecasts : forecastsList) {
            String date = LocalDateTime.parse(forecasts.getDt(),
                    formatter).toLocalDate().toString();
            groupedForecasts.computeIfAbsent(date, k -> new ArrayList<>()).add(forecasts);
        }

        return groupedForecasts.entrySet().stream()
                .map(entry -> {
                    ForecastDetails details = new ForecastDetails();
                    details.setDate(entry.getKey());
                    entry.getValue().forEach(forecast -> details.getSteps().add(createStepUp(forecast, formatter)));
                    return details;
                })
                .collect(Collectors.toList());
    }

    public static StepUp createStepUp(Forecast forecast, DateTimeFormatter formatter) {
        StepUp steps = new StepUp();
        Main main = forecast.getMain();
        int currHour = LocalDateTime.parse(forecast.getDt(), formatter).getHour();
        String formattedTime = String.format("%d %s", currHour % 12 == 0 ? 12 : currHour % 12,
                currHour < 12 ? "AM" : "PM");

        steps.setTemp(main.getTemp());
        steps.setTime(formattedTime);
        steps.setIcon(forecast.getWeather().get(0).getIcon());
        steps.setHumidity(main.getHumidity());
        return steps;
    }

    public static int getApiCallCount() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int apiCallCount = 8 - (hour / 3) + 24;
        if (hour % 3 != 0)
            apiCallCount += 1;
        return apiCallCount;
    }

}
