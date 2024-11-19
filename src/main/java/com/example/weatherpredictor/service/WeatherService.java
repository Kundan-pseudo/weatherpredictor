package com.example.weatherpredictor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.weatherpredictor.utils.Helper;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;
    @Value("${weather.api.url}")
    private String BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<OpenWeatherResponse> getPublicApiWeatherForecast(String city) {
        log.debug("WeatherService::getPublicApiWeatherForecast");
        Integer count = Helper.getApiCallCount();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&cnt=" + count + "&units=metric";
        OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
        return ResponseEntity.ok(response);
    }

    public WeatherResponse getWeatherForecast(String city) {
        log.debug("WeatherService::getWeatherForecast");
        ResponseEntity<OpenWeatherResponse> responseEntity = getPublicApiWeatherForecast(city);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
            return processWeatherData(responseEntity.getBody());
        log.error("Failed to fetch weather data");
        throw new RuntimeException("Failed to fetch weather data");
    }

    private WeatherResponse processWeatherData(OpenWeatherResponse weatherData) {
        log.debug("WeatherService::processWeatherData");
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
}
