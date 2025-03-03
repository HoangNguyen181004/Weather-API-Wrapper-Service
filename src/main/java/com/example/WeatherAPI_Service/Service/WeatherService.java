package com.example.WeatherAPI_Service.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String baseUrl;

    @Value("${weather.cache.ttl}")
    private long cacheTtl;

    public WeatherService(RestTemplate restTemplate, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }

    public String getWeather(String city) {
        String cacheKey = "weather:" + city.trim();
        // Kiểm tra dữ liệu có trong Redis không?
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            System.out.println("Returning cached data for city: " + city);
            return cachedData; // trả về dữ liệu cache nếu có
        }

        // build URL đúng với Visual Crossing API
        String url = baseUrl + city + "?unitGroup=metric&key=" + apiKey + "&contentType=json";
        System.out.println("Calling API: " + url);

        // gọi API nếu cache không có
        String response = restTemplate.getForObject(url, String.class);

        // Lưu vào redis với thời gian 12h)
        redisTemplate.opsForValue().set(cacheKey, response, cacheTtl, TimeUnit.MINUTES);

        return response;
    }
}
