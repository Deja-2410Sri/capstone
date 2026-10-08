package com.cropadvisory.platform.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for weather information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {

    private Long id;
    private String location;
    private Double temperature;
    private Double humidity;
    private Double rainfall;
    private String weatherCondition;
    private LocalDateTime recordedAt;
}
