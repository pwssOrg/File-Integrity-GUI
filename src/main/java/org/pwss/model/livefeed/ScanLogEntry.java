package org.pwss.model.livefeed;

import java.time.LocalTime;

/**
 * One line of the scan log shown while a scan is running.
 *
 * @param kind    what happened
 * @param subject the file name for file results, or the message text for
 *                {@link Kind#INFO} entries
 * @param detail  extra information (the file size for skipped files), may be
 *                empty
 * @param time    when the entry was received
 */
public record ScanLogEntry(Kind kind, String subject, String detail, LocalTime time) {

    /**
     * The kinds of scan log entries.
     */
    public enum Kind {
        /**
         * The file matches its baseline.
         */
        UNCHANGED,
        /**
         * The file differs from its baseline.
         */
        CHANGED,
        /**
         * The file was too large to be hashed and was skipped.
         */
        SKIPPED,
        /**
         * A general message that is not about a single file.
         */
        INFO
    }

    /**
     * Creates an informational entry.
     *
     * @param message the message text
     * @param time    when the entry was received
     * @return the entry
     */
    public static ScanLogEntry info(String message, LocalTime time) {
        return new ScanLogEntry(Kind.INFO, message, "", time);
    }

    /**
     * Describes the entry as a full sentence.
     *
     * @return the description
     */
    public String describe() {
        return switch (kind) {
            case UNCHANGED -> subject + " has not changed since the baseline scan";
            case CHANGED -> subject + " has changed since the baseline scan";
            case SKIPPED -> subject + " (" + detail + ") is bigger than the max file size for hashing and was skipped";
            case INFO -> subject;
        };
    }
}
