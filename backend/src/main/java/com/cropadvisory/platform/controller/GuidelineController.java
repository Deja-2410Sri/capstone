package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.guideline.GuidelineRequest;
import com.cropadvisory.platform.dto.guideline.GuidelineResponse;
import com.cropadvisory.platform.service.GuidelineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for farming guideline endpoints.
 */
@RestController
@RequestMapping("/api/v1/guidelines")
public class GuidelineController {

    private final GuidelineService guidelineService;

    public GuidelineController(GuidelineService guidelineService) {
        this.guidelineService = guidelineService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuidelineResponse>>> getAllGuidelines() {
        return ResponseEntity.ok(ApiResponse.success(guidelineService.getAllGuidelines()));
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<ApiResponse<List<GuidelineResponse>>> getGuidelinesByCropId(@PathVariable Long cropId) {
        return ResponseEntity.ok(ApiResponse.success(guidelineService.getGuidelinesByCropId(cropId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public ResponseEntity<ApiResponse<GuidelineResponse>> createGuideline(@Valid @RequestBody GuidelineRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(guidelineService.createGuideline(request), "Guideline created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public ResponseEntity<ApiResponse<GuidelineResponse>> updateGuideline(
            @PathVariable Long id, @Valid @RequestBody GuidelineRequest request) {
        return ResponseEntity.ok(ApiResponse.success(guidelineService.updateGuideline(id, request), "Guideline updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGuideline(@PathVariable Long id) {
        guidelineService.deleteGuideline(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Guideline deleted successfully"));
    }
}
