package com.cropadvisory.platform.dto.disease;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for disease/pest information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseResponse {

    private Long id;
    private Long cropId;
    private String cropName;
    private String diseaseName;
    private String symptoms;
    private String causes;
    private String prevention;
    private String treatment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
