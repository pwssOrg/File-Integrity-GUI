package org.pwss.util;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.pwss.util.LoginUtil.LoginValidationResult;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUtilTest {

    // --- Helper for readable assertions ---
    private void assertHasError(LoginValidationResult result, String expectedError) {
        assertFalse(result.isValid(), "Expected validation to fail");
        assertTrue(result.errors().contains(expectedError),
                "Expected error message: " + expectedError);
    }

    @Test
    void testAllFieldsEmpty() {
        LoginValidationResult result = LoginUtil.validateInput("", "", "", "", false);
        assertFalse(result.isValid());
        assertEquals(3, result.errors().size());
        assertHasError(result, "Username cannot be empty.");
        assertHasError(result, "Password cannot be empty.");
        assertHasError(result, "License key cannot be empty.");
    }

    @Test
    void testNullInputsHandledGracefully() {
        assertDoesNotThrow(() -> {
            LoginValidationResult result = LoginUtil.validateInput(null, null, null, null, false);
            assertFalse(result.isValid());
            assertTrue(result.errors().contains("Username cannot be empty."));
        });
    }

    // --- License key validation ---

    @Test
    void testMissingLicenseKey() {
        LoginValidationResult result = LoginUtil.validateInput("user", "password", "", "", false);
        assertHasError(result, "License key cannot be empty.");
    }

    // --- Password validation in createUserMode ---

    @Test
    void testPasswordTooShort() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Ab1!", "Ab1!", "key123", true);
        assertHasError(result, "Password must be at least 8 characters long.");
    }

    @Test
    void testConfirmPasswordMissing() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Abcd123!", "", "key123", true);
        assertHasError(result, "Confirm Password cannot be empty.");
    }

    @Test
    void testPasswordsDoNotMatch() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Abcd123!", "Abcd1234!", "key123", true);
        assertHasError(result, "Password and Confirm Password do not match.");
    }

    @Test
    void testPasswordMissingUppercase() {
        LoginValidationResult result = LoginUtil.validateInput("user", "abcd123!", "abcd123!", "key123", true);
        assertHasError(result, "Password must contain at least one uppercase letter.");
    }

    @Test
    void testPasswordMissingDigit() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Abcdefg!", "Abcdefg!", "key123", true);
        assertHasError(result, "Password must contain at least one digit.");
    }

    @Test
    void testPasswordMissingSpecialCharacter() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Abcdefg1", "Abcdefg1", "key123", true);
        assertHasError(result, "Password must contain at least one special character.");
    }

    // --- Valid case ---

    @Test
    void testValidInputInCreateUserMode() {
        LoginValidationResult result = LoginUtil.validateInput("user", "Abcd123!", "Abcd123!", "key123", true);
        assertTrue(result.isValid(), "Expected validation to pass");
        assertTrue(result.errors().isEmpty(), "Expected no errors");
    }

    @Test
    void testValidInputInLoginMode() {
        LoginValidationResult result = LoginUtil.validateInput("user", "password", "", "key123", false);
        assertTrue(result.isValid(), "Expected validation to pass in login mode");
    }

    // --- formatErrors() ---

    @Test
    void testFormatErrors() {
        List<String> errors = List.of("Error 1", "Error 2", "Error 3");
        String formatted = LoginUtil.formatErrors(errors);
        assertEquals("Error 1\nError 2\nError 3", formatted);
    }
}
