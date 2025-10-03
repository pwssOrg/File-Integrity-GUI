package org.pwss.utils;

/**
 * A utility class that holds various string constants used throughout the application.
 */
public final class StringConstants {
    // General purpose strings
    public static final String GENERIC_YES = "Yes";
    public static final String GENERIC_NO = "No";
    public static final String GENERIC_SUCCESS = "Success";
    public static final String GENERIC_ERROR = "Error";

    // Scan related strings
    public static final String SCAN_FULL = "Full scan";
    public static final String SCAN_STOP = "Stop scan";
    public static final String SCAN_STARTED_SUCCESS = "Scan started successfully!";
    public static final String SCAN_STOPPED_SUCCESS = "Scan stopped successfully!";
    public static final String SCAN_STARTED_FAILURE = "Failed to start scan.";
    public static final String SCAN_STOPPED_FAILURE = "Failed to stop scan.";
    public static final String SCAN_START_ERROR = "Error starting scan: ";
    public static final String SCAN_STOP_ERROR = "Error stopping scan: ";
    public static final String SCAN_COMPLETED_DIFFS = "Scan completed with differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";
    public static final String SCAN_COMPLETED_NO_DIFFS = "Scan completed with no differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";
    public static final String SCAN_NOT_COMPLETED = "Scan not completed.";
    public static final String SCAN_DIFFS_PREFIX = "Diffs: ";
    public static final String SCAN_LIVE_FEED_ERROR_PREFIX = "An error occurred while retrieving the live feed: ";
    public static final String SCAN_SHOW_RESULTS_ERROR_PREFIX = "Error displaying scan results: ";

    // Monitored directory popup related strings
    public static final String MON_DIR_POPUP_START_SCAN = "Scan this directory";
    public static final String MON_DIR_POPUP_RESET_BASELINE = "Reset baseline";
    public static final String MON_DIR_POPUP_RESET_BASELINE_POPUP_TITLE = "Confirm Reset Baseline";
    public static final String MON_DIR_POPUP_RESET_BASELINE_POPUP_MESSAGE = "Type the code to confirm baseline reset for:\n";
    public static final String MON_DIR_POPUP_RESET_BASELINE_SUCCESS = "Baseline reset successfully.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR = "Failed to reset baseline.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR_EMPTY_INPUT = "Endpoint code is required. Baseline reset cancelled.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR_INVALID = "Invalid code. Baseline reset cancelled.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR_PREFIX = "Error resetting baseline: ";
    public static final String MON_DIR_POPUP_EDIT_DIR = "Edit this directory";

    // New directory screen related strings
    public static final String NEW_DIR_SUCCESS_TEXT = "Directory added successfully!";
    public static final String NEW_DIR_ERROR_PREFIX = "Failed to add directory: ";
    public static final String NEW_DIR_NO_PATH_SELECTED = "No directory selected.";

    // File search related strings
    public static final String FILE_SEARCH_RESULTS_PREFIX = "Results: ";
}
