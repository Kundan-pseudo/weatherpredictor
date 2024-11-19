package com.example.weatherpredictor.exception;

import com.example.weatherpredictor.dto.HttpClientErrorResponseBody;
import com.example.weatherpredictor.model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherServiceException.class)
    public ResponseEntity<ErrorResponse> handleWeatherServiceException(WeatherServiceException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "Error fetching weather data");
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<HttpClientErrorResponseBody> handleHttpClientException(HttpClientErrorException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        HttpClientErrorResponseBody res = ex.getResponseBodyAs(HttpClientErrorResponseBody.class);
        return new ResponseEntity<>(res, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Internal Server Error", ex.getMessage());
        log.error("Error = " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}