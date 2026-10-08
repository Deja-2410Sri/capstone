package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.disease.DiseaseRequest;
import com.cropadvisory.platform.dto.disease.DiseaseResponse;
import com.cropadvisory.platform.service.DiseaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for disease/pest information endpoints.
 */
@RestController
@RequestMapping("/api/v1/diseases")
public class DiseaseController {

    private final DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiseaseResponse>>> getAllDiseases() {
        return ResponseEntity.ok(ApiResponse.success(diseaseService.getAllDiseases()));
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<ApiResponse<List<DiseaseResponse>>> getDiseasesByCropId(@PathVariable Long cropId) {
        return ResponseEntity.ok(ApiResponse.success(diseaseService.getDiseasesByCropId(cropId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> createDisease(@Valid @RequestBody DiseaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(diseaseService.createDisease(request), "Disease record created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXPERT')")
    public ResponseEntity<ApiResponse<DiseaseResponse>> updateDisease(
            @PathVariable Long id, @Valid @RequestBody DiseaseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(diseaseService.updateDisease(id, request), "Disease record updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDisease(@PathVariable Long id) {
        diseaseService.deleteDisease(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Disease record deleted successfully"));
    }
}
