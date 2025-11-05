package org.pwss.utils;

/**
 * Utility class for processing live feed entries.
 */
public final class LiveFeedUtils {

    /**
     * A white check mark emoji used in live feed entries.
     */
    private final static String WHITE_CHECK_MARK = "✅";
    /**
     * The white check mark emoji with a newline character appended to it.
     */
    private final static String WHITE_CHECK_MARK_REPLACE = "✅\n";

    /**
     * A warning emoji used in live feed entries.
     */
    private final static String WARNING = "⚠️";
    /**
     * The warning emoji with a newline character appended to it.
     */
    private final static String WARNING_REPLACE = "⚠️\n";

    /**
     * A message indicating that a file is too big, used in live feed entries.
     */
    private final static String FILE_TO_BIG_MESSAGE = "is bigger than the user defined max limit";
    /**
     * The file too big message with a newline character appended to it.
     */
    private final static String FILE_TO_BIG_MESSAGE_REPLACE = FILE_TO_BIG_MESSAGE + "\n";

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
                .replace(WHITE_CHECK_MARK, WHITE_CHECK_MARK_REPLACE)
                .replace(WARNING, WARNING_REPLACE)
                .replace(FILE_TO_BIG_MESSAGE, FILE_TO_BIG_MESSAGE_REPLACE)
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

        return (int) entry.codePoints()
                .filter(c -> c == 0x26A0) // ⚠
                .count();
    }
}
