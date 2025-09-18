package org.pwss.utils;

public final class LiveFeedUtils {

    /**
     * Formats a raw live feed entry for improved readability.
     * Currently, inserts line breaks after certain emojis.
     *
     * @param rawEntry the raw live feed text from the service
     * @return formatted live feed text
     */
    public static String formatLiveFeedEntry(String rawEntry) {
        if (rawEntry == null || rawEntry.isEmpty()) {
            return "";
        }

        return rawEntry
                .replace("✅", "✅\n")
                .replace("⚠️", "⚠️\n")
                .trim();
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
        // Split by the warning symbol and subtract 1 to get the count
        return entry.split("⚠️").length - 1;
    }
}
