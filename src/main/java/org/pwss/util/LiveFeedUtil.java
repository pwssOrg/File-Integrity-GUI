package org.pwss.util;

/**
 * Utility class for processing live feed entries.
 */
public final class LiveFeedUtil {

    private LiveFeedUtil() {
    }

    /**
     * Counts the number of warnings in a live feed entry.
     * Warnings are indicated by the "⚠" symbol.
     *
     * @param entry the live feed entry text
     * @return the count of warnings in the entry
     */
    public static int countWarnings(String entry) {
        if (entry == null || entry.isEmpty()) {
            return 0;
        }

        return (int) entry.codePoints()
                .filter(c -> c == 0x26A0) // ⚠
                .count();
    }
}
