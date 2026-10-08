package com.cropadvisory.platform.dto.guideline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for farming guideline information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuidelineResponse {

    private Long id;
    private Long cropId;
    private String cropName;
    private String sowingMethod;
    private String irrigation;
    private String fertilizer;
    private String pestManagement;
    private String harvesting;
    private String generalTips;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
