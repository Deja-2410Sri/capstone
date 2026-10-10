package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.auth.AuthResponse;
import com.cropadvisory.platform.dto.auth.LoginRequest;
import com.cropadvisory.platform.dto.auth.RegisterRequest;
import com.cropadvisory.platform.dto.auth.RegistrationResponse;
import com.cropadvisory.platform.exception.BadRequestException;
import com.cropadvisory.platform.exception.UnauthorizedException;
import com.cropadvisory.platform.model.entity.FarmerProfile;
import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.model.enums.Role;
import com.cropadvisory.platform.repository.FarmerProfileRepository;
import com.cropadvisory.platform.repository.UserRepository;
import com.cropadvisory.platform.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailOtpService emailOtpService;

    public AuthService(UserRepository userRepository,
                       FarmerProfileRepository farmerProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       EmailOtpService emailOtpService) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailOtpService = emailOtpService;
    }

    @Transactional
    public RegistrationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(Role.ROLE_FARMER)
                .enabled(true)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        FarmerProfile farmerProfile = FarmerProfile.builder()
                .user(user)
                .build();
        farmerProfileRepository.save(farmerProfile);

        emailOtpService.sendOtp(user);
        log.info("Registration created; email verification pending for {}", user.getEmail());

        return RegistrationResponse.builder()
                .email(user.getEmail())
                .message("Registration created. Check your email for the verification code.")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new BadRequestException("Please verify your email before logging in");
        }

        String token = generateJwtToken(user);
        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }


    @Transactional
    public void verifyEmailOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or OTP"));
        try {
            emailOtpService.verifyOtp(user, otp);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
    private String generateJwtToken(User user) {
        return jwtService.generateToken(
                Collections.singletonMap("userId", user.getId()),
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPasswordHash(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );
    }
}

