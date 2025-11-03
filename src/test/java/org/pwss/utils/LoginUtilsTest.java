package org.pwss.utils;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUtilsTest {

    @Test
    void validateInput_returnsValidResult_whenAllInputsAreValid() {
        var result = LoginUtils.validateInput("validUser", "Valid123!", "LICENSE123", false);
        assertTrue(result.isValid());
        assertNull(result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenUsernameIsEmpty() {
        var result = LoginUtils.validateInput("", "Valid123!", "LICENSE123", false);
        assertFalse(result.isValid());
        assertEquals("Username cannot be empty.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenPasswordIsEmpty() {
        var result = LoginUtils.validateInput("validUser", "", "LICENSE123", false);
        assertFalse(result.isValid());
        assertEquals("Password cannot be empty.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenPasswordTooShortInCreateUserMode() {
        var result = LoginUtils.validateInput("validUser", "Short1!", "LICENSE123", true);
        assertFalse(result.isValid());
        assertEquals("Password must be at least 8 characters long.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenPasswordMissingUppercaseInCreateUserMode() {
        var result = LoginUtils.validateInput("validUser", "valid123!", "LICENSE123", true);
        assertFalse(result.isValid());
        assertEquals("Password must contain at least one uppercase letter.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenPasswordMissingDigitInCreateUserMode() {
        var result = LoginUtils.validateInput("validUser", "ValidPass!", "LICENSE123", true);
        assertFalse(result.isValid());
        assertEquals("Password must contain at least one digit.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenPasswordMissingSpecialCharacterInCreateUserMode() {
        var result = LoginUtils.validateInput("validUser", "Valid1234", "LICENSE123", true);
        assertFalse(result.isValid());
        assertEquals("Password must contain at least one special character.", result.errorMessage());
    }

    @Test
    void validateInput_returnsError_whenLicenseKeyIsEmpty() {
        var result = LoginUtils.validateInput("validUser", "Valid123!", "", false);
        assertFalse(result.isValid());
        assertEquals("License key cannot be empty.", result.errorMessage());
    }
}
