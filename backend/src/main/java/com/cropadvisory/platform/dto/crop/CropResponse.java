package com.cropadvisory.platform.dto.crop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for crop information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropResponse {

    private Long id;
    private String cropName;
    private String category;
    private String season;
    private String soilType;
    private String waterRequirement;
    private String temperatureRange;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
