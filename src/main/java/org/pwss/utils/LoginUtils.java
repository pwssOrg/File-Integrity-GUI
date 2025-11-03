package org.pwss.utils;

/**
 * Utility class for validating login inputs.
 */
public final class LoginUtils {

    // Private constructor to prevent instantiation
    private LoginUtils() {
        // This constructor is intentionally empty.
    }
    /**
     * Result of login validation.
     *
     * @param isValid      True if the input is valid, false otherwise.
     * @param errorMessage The error message if invalid, null if valid.
     */
    public record LoginValidationResult(boolean isValid, String errorMessage) {}

    /**
     * Validates the user input for username, password, and license key.
     *
     * @param username       The username input.
     * @param password       The password input.
     * @param licenseKey     The license key input.
     * @param createUserMode True if in user creation mode, false otherwise.
     * @return A LoginValidationResult indicating whether the input is valid and any error message.
     */
    public static LoginValidationResult validateInput(String username, String password, String licenseKey, boolean createUserMode) {
        if (username == null || username.trim().isEmpty()) {
            return new LoginValidationResult(false, "Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginValidationResult(false, "Password cannot be empty.");
        }
        if (createUserMode && password.length() < 8) {
            return new LoginValidationResult(false, "Password must be at least 8 characters long.");
        }
        if (createUserMode && !password.matches(".*[A-Z].*")) {
            return new LoginValidationResult(false, "Password must contain at least one uppercase letter.");
        }
        if (createUserMode && !password.matches(".*\\d.*")) {
            return new LoginValidationResult(false, "Password must contain at least one digit.");
        }
        if (createUserMode && !password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return new LoginValidationResult(false, "Password must contain at least one special character.");
        }
        if (licenseKey == null || licenseKey.trim().isEmpty()) {
            return new LoginValidationResult(false, "License key cannot be empty.");
        }
        return new LoginValidationResult(true, null);
    }
}
