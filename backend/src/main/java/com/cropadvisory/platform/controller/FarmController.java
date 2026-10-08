package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.common.PageResponse;
import com.cropadvisory.platform.dto.farm.FarmRequest;
import com.cropadvisory.platform.dto.farm.FarmResponse;
import com.cropadvisory.platform.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for farm management endpoints.
 */
@RestController
@RequestMapping("/api/v1/farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FarmResponse>>> getFarms(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<FarmResponse> farms = farmService.getFarmsByEmail(authentication.getName(), PageRequest.of(page, size, sort));
        PageResponse<FarmResponse> pageResponse = PageResponse.<FarmResponse>builder()
                .content(farms.getContent())
                .page(farms.getNumber())
                .size(farms.getSize())
                .totalElements(farms.getTotalElements())
                .totalPages(farms.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmResponse>> getFarmById(
            @PathVariable Long id, Authentication authentication) {
        FarmResponse response = farmService.getFarmById(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FarmResponse>> createFarm(
            Authentication authentication, @Valid @RequestBody FarmRequest request) {
        FarmResponse response = farmService.createFarm(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Farm created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmResponse>> updateFarm(
            @PathVariable Long id, Authentication authentication, @Valid @RequestBody FarmRequest request) {
        FarmResponse response = farmService.updateFarm(id, authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Farm updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFarm(
            @PathVariable Long id, Authentication authentication) {
        farmService.deleteFarm(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Farm deleted successfully"));
    }
}
