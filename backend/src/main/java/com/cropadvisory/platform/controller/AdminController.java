package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.common.PageResponse;
import com.cropadvisory.platform.model.entity.AuditLog;
import com.cropadvisory.platform.model.enums.AdviceStatus;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.repository.*;
import com.cropadvisory.platform.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for admin management endpoints.
 */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    private final CropRepository cropRepository;
    private final CropRecommendationRepository recommendationRepository;
    private final ExpertAdviceRepository adviceRepository;
    private final DiseasePestRepository diseasePestRepository;
    private final NotificationRepository notificationRepository;
    private final AuditLogService auditLogService;

    public AdminController(UserRepository userRepository,
                           FarmRepository farmRepository,
                           CropRepository cropRepository,
                           CropRecommendationRepository recommendationRepository,
                           ExpertAdviceRepository adviceRepository,
                           DiseasePestRepository diseasePestRepository,
                           NotificationRepository notificationRepository,
                           AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.farmRepository = farmRepository;
        this.cropRepository = cropRepository;
        this.recommendationRepository = recommendationRepository;
        this.adviceRepository = adviceRepository;
        this.diseasePestRepository = diseasePestRepository;
        this.notificationRepository = notificationRepository;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalFarmers", userRepository.count() - 1);
        stats.put("totalCrops", cropRepository.count());
        stats.put("totalRecommendations", recommendationRepository.count());
        stats.put("openExpertQuestions", adviceRepository.countByStatus(AdviceStatus.OPEN));
        stats.put("diseasePestRecords", diseasePestRepository.count());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<User>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        PageResponse<User> pageResponse = PageResponse.<User>builder()
                .content(users.getContent())
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/crops")
    public ResponseEntity<ApiResponse<PageResponse<Object>>> getCrops(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
       Page<Object> crops = (Page<Object>) (Page<?>) cropRepository.findAll(PageRequest.of(page, size));
        PageResponse<Object> pageResponse = PageResponse.<Object>builder()
                .content(crops.getContent())
                .page(crops.getNumber())
                .size(crops.getSize())
                .totalElements(crops.getTotalElements())
                .totalPages(crops.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/advice")
    public ResponseEntity<ApiResponse<PageResponse<Object>>> getAdvice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Object> advice = (Page<Object>) (Page<?>) adviceRepository.findAll(PageRequest.of(page, size));
        PageResponse<Object> pageResponse = PageResponse.<Object>builder()
                .content(advice.getContent())
                .page(advice.getNumber())
                .size(advice.getSize())
                .totalElements(advice.getTotalElements())
                .totalPages(advice.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AuditLog> logs = auditLogService.getAllAuditLogs(PageRequest.of(page, size));
        PageResponse<AuditLog> pageResponse = PageResponse.<AuditLog>builder()
                .content(logs.getContent())
                .page(logs.getNumber())
                .size(logs.getSize())
                .totalElements(logs.getTotalElements())
                .totalPages(logs.getTotalPages())
                .build();
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
}
