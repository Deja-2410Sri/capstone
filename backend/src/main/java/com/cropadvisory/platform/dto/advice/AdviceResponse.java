package com.cropadvisory.platform.dto.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for expert advice information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceResponse {

    private Long id;
    private Long farmerId;
    private String farmerName;
    private Long expertId;
    private String expertName;
    private String question;
    private String response;
    private String status;
    private LocalDateTime askedAt;
    private LocalDateTime answeredAt;
}
