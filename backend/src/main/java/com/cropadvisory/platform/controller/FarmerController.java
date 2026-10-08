package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.farmer.FarmerProfileResponse;
import com.cropadvisory.platform.dto.farmer.FarmerProfileUpdateRequest;
import com.cropadvisory.platform.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for farmer profile management.
 */
@RestController
@RequestMapping("/api/v1/profile")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getProfile(Authentication authentication) {
        FarmerProfileResponse response = farmerService.getProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> updateProfile(
            Authentication authentication,
            @RequestBody FarmerProfileUpdateRequest request) {
        FarmerProfileResponse response = farmerService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }
}
