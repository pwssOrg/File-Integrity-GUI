package org.pwss.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating login inputs.
 */
public final class LoginUtil {

    // Private constructor to prevent instantiation
    private LoginUtil() {
        // This constructor is intentionally empty.
    }

    /**
     * Validates the login input fields.
     *
     * @param username        The username input.
     * @param password        The password input.
     * @param confirmPassword The confirm password input (used in create user mode).
     * @param licenseKey      The license key input.
     * @param createUserMode  Flag indicating if the application is in create user mode.
     * @return A LoginValidationResult containing validation status and error messages.
     */
    public static LoginValidationResult validateInput(
            String username,
            String password,
            String confirmPassword,
            String licenseKey,
            boolean createUserMode
    ) {
        List<String> errors = new ArrayList<>();

        username = username == null ? "" : username.trim();
        password = password == null ? "" : password.trim();
        confirmPassword = confirmPassword == null ? "" : confirmPassword.trim();
        licenseKey = licenseKey == null ? "" : licenseKey.trim();

        if (username.isEmpty()) errors.add("Username cannot be empty.");
        if (password.isEmpty()) errors.add("Password cannot be empty.");
        if (licenseKey.isEmpty()) errors.add("License key cannot be empty.");

        if (createUserMode && !password.isEmpty()) {
            if (password.length() < 8)
                errors.add("Password must be at least 8 characters long.");

            if (confirmPassword.isEmpty())
                errors.add("Confirm Password cannot be empty.");
            else if (!xorEquals(password, confirmPassword))
                errors.add("Password and Confirm Password do not match.");

            if (!password.matches(".*[A-Z].*"))
                errors.add("Password must contain at least one uppercase letter.");
            if (!password.matches(".*\\d.*"))
                errors.add("Password must contain at least one digit.");
            if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
                errors.add("Password must contain at least one special character.");
        }

        return new LoginValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Formats a list of error messages into a single string.
     *
     * @param errors List of error messages.
     * @return Formatted error string.
     */
    public static String formatErrors(List<String> errors) {
        return String.join("\n", errors);
    }


    /**
     * Compares two strings for equality using XOR operation.
     *
     * @param s1 First string.
     * @param s2 Second string.
     * @return True if both strings are equal, false otherwise.
     * @throws IllegalArgumentException if either string is null.
     */
    private static boolean xorEquals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Strings to compare cannot be null");
        }

        byte[] a = s1.getBytes(StandardCharsets.UTF_8);
        byte[] b = s2.getBytes(StandardCharsets.UTF_8);

        int maxLen = Math.max(a.length, b.length);
        int result = a.length ^ b.length;

        for (int i = 0; i < maxLen; i++) {
            byte ba = (i < a.length) ? a[i] : 0;
            byte bb = (i < b.length) ? b[i] : 0;
            result |= (ba ^ bb);
        }

        return result == 0;
    }

    /**
     * Result of login validation.
     *
     * @param isValid True if the input is valid, false otherwise.
     * @param errors  List of error messages if the input is invalid.
     */
    public record LoginValidationResult(boolean isValid, List<String> errors) {
    }
}

