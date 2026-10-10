package com.cropadvisory.platform.dto.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerifyOtpRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterAll
    static void tearDown() {
        Validation.buildDefaultValidatorFactory().close();
    }

    @Test
    void acceptsValidEmailAndSixDigitOtp() {
        VerifyOtpRequest request = new VerifyOtpRequest("farmer@example.com", "123456");
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsOtpWithFewerThanSixDigits() {
        VerifyOtpRequest request = new VerifyOtpRequest("farmer@example.com", "12345");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsOtpContainingLetters() {
        VerifyOtpRequest request = new VerifyOtpRequest("farmer@example.com", "12A456");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsInvalidEmail() {
        VerifyOtpRequest request = new VerifyOtpRequest("not-an-email", "123456");
        assertFalse(validator.validate(request).isEmpty());
    }
}
