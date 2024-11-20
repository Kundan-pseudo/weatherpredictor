package com.example.weatherpredictor.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.weatherpredictor.model.*;

public class Helper {

    private Helper() {

    }

    private static final int[] updateHours = {3, 6, 9, 12, 15, 18, 21, 24};

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
                .toList();
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

    public static WeatherResponse processWeatherData(OpenWeatherResponse weatherData) {
        WeatherResponse weatherResponse = new WeatherResponse();
        Current current = new Current();
        Forecast currForecast = weatherData.getList().get(1);
        Main currMain = currForecast.getMain();
        current.setTemp(currMain.getTemp());
        current.setFeelsLike(currMain.getFeelsLike());
        current.setHumidity(currMain.getHumidity());
        current.setHighTemp(currMain.getTempMax());
        current.setLowTemp(currMain.getTempMin());
        current.setWind(currForecast.getWind().getSpeed());
        current.setDate(currForecast.getDt());
        current.setAdvisory(Helper.getAdvisory(currForecast, currMain));
        weatherResponse.setCurrent(current);
        weatherResponse.setCity(weatherData.getCity());

        weatherResponse.setCurrent(current);
        weatherResponse.setCity(weatherData.getCity());
        weatherResponse.setForecast(Helper.groupForecasts(weatherData.getList()));

        return weatherResponse;
    }

    public static int calcExpiryTime(int expiryInSec, LocalDateTime now) {
        for (int hour: updateHours) {
            if (now.getHour() == hour - 1 && now.getMinute() >= 60 - expiryInSec / 60) {
                return (60 - now.getMinute()) * 60 - now.getSecond();
            }
        }
        return expiryInSec;
    }

}
