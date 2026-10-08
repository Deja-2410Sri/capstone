package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.auth.AuthResponse;
import com.cropadvisory.platform.dto.auth.LoginRequest;
import com.cropadvisory.platform.dto.auth.RegisterRequest;
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

/**
 * Service handling user authentication and registration.
 *
 * <p>Manages JWT-based authentication, user registration with role assignment,
 * and farmer profile creation for new registrations.</p>
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       FarmerProfileRepository farmerProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user with FARMER role.
     *
     * @param request the registration request containing user details
     * @return authentication response with JWT token
     * @throws BadRequestException if email is already registered
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
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
                .build();

        user = userRepository.save(user);

        FarmerProfile farmerProfile = FarmerProfile.builder()
                .user(user)
                .build();
        farmerProfileRepository.save(farmerProfile);

        String token = generateJwtToken(user);
        log.info("New farmer registered: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the login request containing credentials
     * @return authentication response with JWT token
     * @throws UnauthorizedException if credentials are invalid
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

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

    /**
     * Returns the current authenticated user's information.
     *
     * @param email the user's email from the JWT token
     * @return user information
     */
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
