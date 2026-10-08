package com.cropadvisory.platform.dto.farmer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for farmer profile information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerProfileResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String location;
    private String address;
    private String district;
    private String state;
    private String postalCode;
    private String soilType;
    private Double farmSize;
}
