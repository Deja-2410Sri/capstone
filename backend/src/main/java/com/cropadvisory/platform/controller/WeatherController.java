package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.weather.WeatherResponse;
import com.cropadvisory.platform.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for weather information endpoints.
 */
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{location}")
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeatherByLocation(@PathVariable String location) {
        WeatherResponse response = weatherService.getWeatherByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
