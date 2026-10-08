package com.cropadvisory.platform.dto.disease;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a disease/pest record.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseRequest {

    private Long cropId;

    @NotBlank(message = "Disease name is required")
    private String diseaseName;

    private String symptoms;

    private String causes;

    private String prevention;

    private String treatment;
}
