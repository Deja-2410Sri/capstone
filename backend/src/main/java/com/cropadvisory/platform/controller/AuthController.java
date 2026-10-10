package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.auth.AuthResponse;
import com.cropadvisory.platform.dto.auth.LoginRequest;
import com.cropadvisory.platform.dto.auth.RegisterRequest;
import com.cropadvisory.platform.dto.auth.RegistrationResponse;
import com.cropadvisory.platform.dto.auth.VerifyOtpRequest;
import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        RegistrationResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Verification code sent to your email"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyEmailOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(ApiResponse.success(null, "Email verified successfully. You can now log in."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser(Authentication authentication) {
        AuthResponse response = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
