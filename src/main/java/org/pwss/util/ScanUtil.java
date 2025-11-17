package org.pwss.util;

/**
 * Utility class for constructing scan-related messages.
 */
public final class ScanUtil {

    /**
     * Prefix string used in scan completion messages indicating the number of
     * differences found.
     */
    private static final String SCAN_COMPLETED_DIFFS_PREFIX = "Scan completed with ";

    /**
     * Suffix string used in scan completion messages providing additional
     * information about viewing details.
     */
    private static final String SCAN_COMPLETED_DIFFS_SUFFIX = " differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ScanUtil() {
        // Prevent instantiation
    }

    /**
     * Constructs a scan completion message with the specified number of differences
     * found.
     *
     * @param diffNumber The number of differences found during the scan.
     * @return A string containing the complete scan message.
     */
    public static String constructDiffMessageString(long diffNumber) {
        return SCAN_COMPLETED_DIFFS_PREFIX + diffNumber + SCAN_COMPLETED_DIFFS_SUFFIX;
    }
}
