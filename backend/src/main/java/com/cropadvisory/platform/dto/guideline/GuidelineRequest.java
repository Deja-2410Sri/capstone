package com.cropadvisory.platform.dto.guideline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a farming guideline.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuidelineRequest {

    private Long cropId;

    private String sowingMethod;

    private String irrigation;

    private String fertilizer;

    private String pestManagement;

    private String harvesting;

    private String generalTips;
}
