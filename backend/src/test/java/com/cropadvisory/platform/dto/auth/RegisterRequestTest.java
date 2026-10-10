package com.cropadvisory.platform.dto.auth;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

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
    void acceptsValidRegistration() {
        RegisterRequest request = new RegisterRequest(
                "farmer@example.com", "secure123", "Ravi", "Kumar", "9876543210");
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsInvalidEmail() {
        RegisterRequest request = new RegisterRequest(
                "invalid-email", "secure123", "Ravi", "Kumar", null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsShortPassword() {
        RegisterRequest request = new RegisterRequest(
                "farmer@example.com", "12345", "Ravi", "Kumar", null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsBlankFirstName() {
        RegisterRequest request = new RegisterRequest(
                "farmer@example.com", "secure123", " ", "Kumar", null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsMissingLastName() {
        RegisterRequest request = new RegisterRequest(
                "farmer@example.com", "secure123", "Ravi", "", null);
        assertFalse(validator.validate(request).isEmpty());
    }
}
