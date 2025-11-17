package org.pwss.util;

/**
 * A utility class for string manipulation operations.
 * This class contains static methods to perform various string manipulations.
 */
public final class StringUtil {

    /**
     * 
     * Private constructor to prevent instantiation
     **/
    private StringUtil() {
    }

    /**
     * Adds a space character at the end of the given string.
     *
     * @param str The input string. If it is null, null is returned.
     * @return A new string with a space appended to the end of the input string,
     *         or null if the input was null.
     */
    public static String addSpace(String str) {
        if (str == null)
            return null;
        return str + " ";
    }

    /**
     * Adds a space character at the beginning of the given string.
     *
     * @param str The input string. If it is null, null is returned.
     * @return A new string with a space prepended to the beginning of the input
     *         string,
     *         or null if the input was null.
     */
    public static String prependSpace(String str) {
        if (str == null)
            return null;
        return " " + str;
    }

    /**
     * Pads the given string on the right with spaces until it reaches the specified
     * length.
     *
     * @param str    The input string. If it is null, null is returned.
     * @param length The desired length of the resulting string. If the input
     *               string's
     *               length is already greater than or equal to this value, the
     *               original
     *               string will be returned unchanged.
     * @return A new string with spaces appended on the right until it reaches the
     *         specified
     *         length, or null if the input was null.
     */
    public static String padRight(String str, int length) {
        if (str == null)
            return null;
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * Pads the given string on the left with spaces until it reaches the specified
     * length.
     *
     * @param str    The input string. If it is null, null is returned.
     * @param length The desired length of the resulting string. If the input
     *               string's
     *               length is already greater than or equal to this value, the
     *               original
     *               string will be returned unchanged.
     * @return A new string with spaces prepended on the left until it reaches the
     *         specified
     *         length, or null if the input was null.
     */
    public static String padLeft(String str, int length) {
        if (str == null)
            return null;
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - str.length()) {
            sb.append(' ');
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * Capitalizes the first letter of the given string.
     *
     * @param str The input string. If it is null or empty, the original string will
     *            be returned.
     * @return A new string with the first letter capitalized, or the original
     *         string if
     *         it was null or empty.
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str))
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Checks if a given string is empty or null.
     *
     * @param str the string to check, can be null
     * @return true if the string is null or has length 0, false otherwise
     */
    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}