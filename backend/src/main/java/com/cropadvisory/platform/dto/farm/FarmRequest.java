package com.cropadvisory.platform.dto.farm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a farm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmRequest {

    private String farmName;

    private String location;

    private String soilType;

    @NotNull(message = "Farm area is required")
    @Positive(message = "Farm area must be positive")
    private Double area;

    private String irrigationType;

    private String waterAvailability;
}
