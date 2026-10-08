package com.cropadvisory.platform.dto.farm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for farm information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmResponse {

    private Long id;
    private String farmName;
    private String location;
    private String soilType;
    private Double area;
    private String irrigationType;
    private String waterAvailability;
    private Long farmerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
