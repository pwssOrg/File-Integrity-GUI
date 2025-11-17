package org.pwss.util;

/**
 * Enum representing different application themes.
 * Each theme has an associated integer value and a display name.
 */
public enum AppTheme {
    DARK(1, "Dark"),
    LIGHT(2, "Light"),
    MAC_LIGHT(3, "Mac Light"),
    MAC_DARK(4, "Mac Dark");

    private final int value;
    private final String displayName;

    AppTheme(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}
