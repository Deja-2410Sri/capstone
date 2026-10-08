package com.cropadvisory.platform.dto.crop;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a crop.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropRequest {

    @NotBlank(message = "Crop name is required")
    private String cropName;

    private String category;

    private String season;

    private String soilType;

    private String waterRequirement;

    private String temperatureRange;

    private String description;

    private Boolean active;
}
