package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.advice.AdviceAnswerRequest;
import com.cropadvisory.platform.dto.advice.AdviceRequest;
import com.cropadvisory.platform.dto.advice.AdviceResponse;
import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.service.ExpertAdviceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for expert advice consultation endpoints.
 */
@RestController
@RequestMapping("/api/v1/advice")
public class ExpertAdviceController {

    private final ExpertAdviceService adviceService;

    public ExpertAdviceController(ExpertAdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdviceResponse>> askExpert(
            Authentication authentication, @Valid @RequestBody AdviceRequest request) {
        AdviceResponse response = adviceService.askExpert(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Question submitted successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> getAdvice(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = authentication.getName();
        Page<AdviceResponse> advice;
        // Check roles to determine which advice to return
        boolean isExpert = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EXPERT"));
        if (isExpert) {
            advice = adviceService.getAdviceForExpert(email, PageRequest.of(page, size));
        } else {
            advice = adviceService.getAdviceForFarmer(email, PageRequest.of(page, size));
        }
        return ResponseEntity.ok(ApiResponse.success(advice));
    }

    @PutMapping("/{id}/answer")
    public ResponseEntity<ApiResponse<AdviceResponse>> answerQuestion(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody AdviceAnswerRequest request) {
        AdviceResponse response = adviceService.answerQuestion(id, authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Answer submitted successfully"));
    }
}
