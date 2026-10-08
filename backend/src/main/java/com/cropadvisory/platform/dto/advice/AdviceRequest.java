package com.cropadvisory.platform.dto.advice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for asking an expert a question.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceRequest {

    @NotBlank(message = "Question is required")
    private String question;
}
