package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.recommendation.RecommendationResponse;
import com.cropadvisory.platform.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for crop recommendation endpoints.
 */
@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> generateRecommendations(
            Authentication authentication) {
        List<RecommendationResponse> recommendations = recommendationService.generateRecommendations(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(recommendations, "Recommendations generated successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getRecommendations(
            Authentication authentication) {
        List<RecommendationResponse> recommendations = recommendationService.getRecommendations(authentication.getName(), null).getContent();
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendationById(@PathVariable Long id) {
        RecommendationResponse response = recommendationService.getRecommendationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
