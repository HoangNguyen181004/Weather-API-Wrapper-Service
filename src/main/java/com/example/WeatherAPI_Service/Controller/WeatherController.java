package com.example.WeatherAPI_Service.Controller;

import com.example.WeatherAPI_Service.Service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        return ResponseEntity.ok(weatherService.getWeather(city));
    }
}
