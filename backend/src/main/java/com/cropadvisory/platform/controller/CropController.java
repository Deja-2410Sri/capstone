package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.common.PageResponse;
import com.cropadvisory.platform.dto.crop.CropRequest;
import com.cropadvisory.platform.dto.crop.CropResponse;
import com.cropadvisory.platform.service.CropService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for crop management endpoints.
 */
@RestController
@RequestMapping("/api/v1/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CropResponse>>> getCrops(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String soilType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CropResponse> crops = cropService.searchCrops(name, category, season, soilType, PageRequest.of(page, size));
        PageResponse<CropResponse> pageResponse = PageResponse.<CropResponse>builder()
                .content(crops.getContent())
                .page(crops.getNumber())
                .size(crops.getSize())
                .totalElements(crops.getTotalElements())
                .totalPages(crops.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CropResponse>> getCropById(@PathVariable Long id) {
        CropResponse response = cropService.getCropById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public ResponseEntity<ApiResponse<CropResponse>> createCrop(@Valid @RequestBody CropRequest request) {
        CropResponse response = cropService.createCrop(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Crop created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CropResponse>> updateCrop(
            @PathVariable Long id, @Valid @RequestBody CropRequest request) {
        CropResponse response = cropService.updateCrop(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Crop updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Crop deleted successfully"));
    }
}
