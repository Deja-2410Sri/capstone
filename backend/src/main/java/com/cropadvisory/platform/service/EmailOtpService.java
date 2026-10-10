package com.cropadvisory.platform.service;

import com.cropadvisory.platform.model.entity.User;
import com.cropadvisory.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailOtpService {

    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final int RESEND_COOLDOWN_SECONDS = 60;
    private static final int MAX_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public EmailOtpService(UserRepository userRepository,
                           JavaMailSender mailSender,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendOtp(User user) {
        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new IllegalStateException("Email is already verified");
        }

        LocalDateTime now = LocalDateTime.now();
        if (user.getVerificationOtpLastSentAt() != null
                && user.getVerificationOtpLastSentAt()
                .plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(now)) {
            throw new IllegalStateException("Please wait before requesting another OTP");
        }

        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        user.setVerificationOtpHash(passwordEncoder.encode(otp));
        user.setVerificationOtpExpiresAt(now.plusMinutes(OTP_EXPIRY_MINUTES));
        user.setVerificationOtpAttempts(0);
        user.setVerificationOtpLastSentAt(now);
        userRepository.save(user);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            if (mailUsername != null && !mailUsername.isBlank()) {
                message.setFrom(mailUsername);
            }
            message.setSubject("Crop Advisory Platform - Email Verification");
            message.setText("Your email verification OTP is: " + otp
                    + "\n\nThis code expires in 10 minutes. Do not share it with anyone.");
            mailSender.send(message);
        } catch (RuntimeException exception) {
            user.setVerificationOtpHash(null);
            user.setVerificationOtpExpiresAt(null);
            userRepository.save(user);
            throw new IllegalStateException(
                    "Unable to send verification email. Check the email service configuration.");
        }
    }

    @Transactional
    public void verifyOtp(User user, String submittedOtp) {
        LocalDateTime now = LocalDateTime.now();

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        if (user.getVerificationOtpAttempts() != null
                && user.getVerificationOtpAttempts() >= MAX_ATTEMPTS) {
            throw new IllegalStateException("Too many attempts. Request a new OTP later.");
        }

        if (user.getVerificationOtpHash() == null
                || user.getVerificationOtpExpiresAt() == null
                || user.getVerificationOtpExpiresAt().isBefore(now)) {
            throw new IllegalStateException("OTP is invalid or expired. Request a new OTP.");
        }

        user.setVerificationOtpAttempts(
                user.getVerificationOtpAttempts() == null
                        ? 1 : user.getVerificationOtpAttempts() + 1);

        if (!passwordEncoder.matches(submittedOtp, user.getVerificationOtpHash())) {
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid OTP");
        }

        user.setEmailVerified(true);
        user.setVerificationOtpHash(null);
        user.setVerificationOtpExpiresAt(null);
        user.setVerificationOtpAttempts(0);
        user.setVerificationOtpLastSentAt(null);
        userRepository.save(user);
    }
}

