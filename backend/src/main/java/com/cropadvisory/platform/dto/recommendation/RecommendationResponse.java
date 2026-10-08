package com.cropadvisory.platform.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for crop recommendation information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponse {

    private Long id;
    private Long farmerId;
    private Long cropId;
    private String cropName;
    private String season;
    private String soilType;
    private String reason;
    private Double suitabilityScore;
    private String status;
    private LocalDateTime recommendationDate;
}
