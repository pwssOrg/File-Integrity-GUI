package org.pwss.utils;

/**
 * Utility class for handling and formatting error messages.
 */
public final class ErrorUtils {

    /**
     * Formats an error message by combining a constant text with a dynamic error message.
     *
     * @param errorConstantText The constant part of the error message, such as "Error: "
     * @param errorMessageText The dynamic part of the error message that provides specific details
     * @return A formatted error message string combining both parts
     */
    public static String formatErrorMessage(String errorConstantText, String errorMessageText) {
        return errorConstantText + errorMessageText;
    }
}