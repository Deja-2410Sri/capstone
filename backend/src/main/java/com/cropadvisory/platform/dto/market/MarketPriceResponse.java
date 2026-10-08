package com.cropadvisory.platform.dto.market;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for market price information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketPriceResponse {

    private Long id;
    private Long cropId;
    private String cropName;
    private String marketName;
    private String location;
    private BigDecimal price;
    private String unit;
    private LocalDate recordedDate;
}
