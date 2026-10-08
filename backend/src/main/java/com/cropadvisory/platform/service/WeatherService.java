package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.weather.WeatherResponse;
import com.cropadvisory.platform.model.entity.WeatherInformation;
import com.cropadvisory.platform.repository.WeatherInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherInformationRepository weatherRepository;

    @Value("${app.weather.api-url:}")
    private String weatherApiUrl;

    @Value("${app.weather.api-key:}")
    private String weatherApiKey;

    public WeatherService(WeatherInformationRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public WeatherResponse getWeatherByLocation(String location) {

        Optional<WeatherInformation> cached =
                weatherRepository.findByLocation(location);

        if (cached.isPresent()) {
            return mapToResponse(cached.get());
        }

        if (weatherApiKey == null || weatherApiKey.isBlank()
                || weatherApiUrl == null || weatherApiUrl.isBlank()) {

            log.warn("Weather API configuration is missing.");

            return WeatherResponse.builder()
                    .location(location)
                    .build();
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = String.format(
                    "%s/weather?q=%s&appid=%s&units=metric",
                    weatherApiUrl,
                    location,
                    weatherApiKey
            );

            log.info("Calling weather API: {}", url.replace(weatherApiKey, "***"));

            WeatherApiResponse apiResponse =
                    restTemplate.getForObject(url, WeatherApiResponse.class);

            if (apiResponse != null && apiResponse.main != null) {

                WeatherResponse response = WeatherResponse.builder()
                        .location(location)
                        .temperature(apiResponse.main.temp)
                        .humidity(apiResponse.main.humidity)
                        .rainfall(
                         apiResponse.rain != null && apiResponse.rain.oneHour != null
                         ? apiResponse.rain.oneHour
                         : 0.0
)
.weatherCondition(
                                apiResponse.weather != null
                                && !apiResponse.weather.isEmpty()
                                ? apiResponse.weather.get(0).description
                                : null
                        )
                        .recordedAt(LocalDateTime.now())
                        .build();

                return response;
            }

        } catch (Exception e) {
            log.error(
                    "Failed to fetch weather data for {}: {}",
                    location,
                    e.getMessage()
            );
        }

        return WeatherResponse.builder()
                .location(location)
                .build();
    }

    private WeatherResponse mapToResponse(WeatherInformation w) {
        return WeatherResponse.builder()
                .id(w.getId())
                .location(w.getLocation())
                .temperature(w.getTemperature())
                .humidity(w.getHumidity())
                .rainfall(w.getRainfall())
                .weatherCondition(w.getWeatherCondition())
                .recordedAt(w.getRecordedAt())
                .build();
    }

    // OpenWeather response classes
    public static class WeatherApiResponse {

       public Main main;
       public java.util.List<Weather> weather;
       public Rain rain;

        public static class Main {
            public Double temp;
            public Double humidity;
        }

        public static class Weather {
            public String description;
        }

        public static class Rain {
        @JsonProperty("1h")
         public Double oneHour;
         }
    }
}