package com.example.weatherpredictor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.weatherpredictor.model.Current;
import com.example.weatherpredictor.model.Forecast;
import com.example.weatherpredictor.model.Main;
import com.example.weatherpredictor.model.OpenWeatherResponse;
import com.example.weatherpredictor.model.WeatherResponse;
import com.example.weatherpredictor.utils.Constants;
import com.example.weatherpredictor.utils.Helper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;
    @Value("${weather.api.url}")
    private String BASE_URL;

    @CircuitBreaker(name = Constants.WEATHER_SERVICE, fallbackMethod = "getWeatherFallback")
    public ResponseEntity<OpenWeatherResponse> getPublicApiWeatherForecast(String city) {
        Integer count = Helper.getApiCallCount();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&cnt=" + count + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();
        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
        return ResponseEntity.ok(response);
    }

    public WeatherResponse getWeatherForecast(String city) {
        ResponseEntity<OpenWeatherResponse> responseEntity = getPublicApiWeatherForecast(city);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            return processWeatherData(responseEntity.getBody());
        throw new RuntimeException("Failed to fetch weather data");
    }

    private WeatherResponse processWeatherData(OpenWeatherResponse weatherData) {
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

    public ResponseEntity<WeatherResponse> getWeatherFallback(Throwable t) {
        WeatherResponse mockResponse = Helper.getMockData();
        return ResponseEntity.ok(mockResponse);
    }
}