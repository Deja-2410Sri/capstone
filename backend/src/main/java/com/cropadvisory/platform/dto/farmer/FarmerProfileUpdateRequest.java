package com.cropadvisory.platform.dto.farmer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating farmer profile.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerProfileUpdateRequest {

    private String location;
    private String address;
    private String district;
    private String state;
    private String postalCode;
    private String soilType;
    private Double farmSize;
}
