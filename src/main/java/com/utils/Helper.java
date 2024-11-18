package com.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.weatherpredictor.model.City;
import com.example.weatherpredictor.model.Current;
import com.example.weatherpredictor.model.Forecast;
import com.example.weatherpredictor.model.ForecastDetails;
import com.example.weatherpredictor.model.Main;
import com.example.weatherpredictor.model.StepUp;
import com.example.weatherpredictor.model.WeatherResponse;

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

    public static WeatherResponse getMockData() {
        WeatherResponse mockResponse = new WeatherResponse();

        City city = new City();
        city.setName("Kolkata");
        city.setSunrise(1731802846);
        city.setSunset(1731842551);
        mockResponse.setCity(city);

        Current current = new Current("2024-11-17 18:00:00", 23.66, 23.66, 21.04, "broken clouds", 23.41, 1.7, 51);
        mockResponse.setCurrent(current);
        ForecastDetails forecast1 = new ForecastDetails("2024-11-17", Arrays.asList(
                new StepUp("3 PM", "04n", 24.97, 53),
                new StepUp("6 PM", "04n", 23.66, 51),
                new StepUp("9 PM", "02n", 21.34, 53)));

        ForecastDetails forecast2 = new ForecastDetails("2024-11-18", Arrays.asList(
                new StepUp("12", "01n", 18.52, 56),
                new StepUp("3 AM", "01d", 23.52, 43),
                new StepUp("6 AM", "01d", 27.72, 32),
                new StepUp("9 AM", "01d", 28.45, 29),
                new StepUp("12", "01n", 23.69, 39),
                new StepUp("3 PM", "01n", 21.86, 44),
                new StepUp("6 PM", "01n", 20.47, 46),
                new StepUp("9 PM", "01n", 19.56, 52)));

        ForecastDetails forecast3 = new ForecastDetails("2024-11-19", Arrays.asList(
                new StepUp("12", "01n", 18.7, 62),
                new StepUp("3 AM", "01d", 23.94, 52),
                new StepUp("6 AM", "01d", 28.03, 40),
                new StepUp("9 AM", "01d", 29.02, 37),
                new StepUp("12", "01n", 24.92, 50),
                new StepUp("3 PM", "01n", 23.05, 55),
                new StepUp("6 PM", "01n", 21.78, 57),
                new StepUp("9 PM", "01n", 20.62, 59)));

        ForecastDetails forecast4 = new ForecastDetails("2024-11-20", Arrays.asList(
                new StepUp("12", "01n", 19.62, 62),
                new StepUp("3 AM", "01d", 24.42, 51),
                new StepUp("6 AM", "01d", 28.48, 39),
                new StepUp("9 AM", "01d", 29.29, 36),
                new StepUp("12", "01n", 25.17, 48),
                new StepUp("3 PM", "01n", 23.64, 51),
                new StepUp("6 PM", "01n", 22.47, 52),
                new StepUp("9 PM", "01n", 20.79, 57)));

        mockResponse.setForecast(Arrays.asList(forecast1, forecast2, forecast3, forecast4));
        return mockResponse;
    }

}
