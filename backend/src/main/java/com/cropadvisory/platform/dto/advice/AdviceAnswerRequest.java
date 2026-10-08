package com.cropadvisory.platform.dto.advice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for an expert to answer a question.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceAnswerRequest {

    @NotBlank(message = "Response is required")
    private String response;
}
