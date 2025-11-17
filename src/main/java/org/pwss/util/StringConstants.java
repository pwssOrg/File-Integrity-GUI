package org.pwss.util;

/**
 * A utility class that holds various string constants used throughout the application.
 */
public final class StringConstants {
    // General purpose strings
    public static final String GENERIC_YES = "Yes";
    public static final String GENERIC_NO = "No";
    public static final String GENERIC_SUCCESS = "Success";
    public static final String GENERIC_ERROR = "Error";
    public static final String GENERIC_OK = "OK";
    public static final String GENERIC_CANCEL = "Cancel";

    // Home screen related strings
    public static final String TOOLTIP_BASELINE_NOT_ESTABLISHED = "Baseline not established";
    public static final String TOOLTIP_OLD_SCAN = "This monitored directory has not been scanned for at least 7 days.";
    public static final String NOTIFICATION_NO_BASELINE = "⚠️ [No Baseline] ";
    public static final String NOTIFICATION_OLD_SCAN = "⏰ [Scan Overdue] ";
    public static final String NOTIFICATION_NO_MONITORED_DIRS = "No directories are being monitored.";

    // Scan related strings
    public static final String SCAN_FULL = "Full scan";
    public static final String SCAN_STOP = "Stop scan";
    public static final String SCAN_STARTED_SUCCESS = "Scan started successfully!";
    public static final String SCAN_STARTED_BASELINE_SUCCESS = "Establishing baseline";
    public static final String SCAN_STOPPED_SUCCESS = "Scan stopped successfully!";
    public static final String SCAN_STARTED_FAILURE = "Failed to start scan.";
    public static final String SCAN_STOPPED_FAILURE = "Failed to stop scan.";
    public static final String SCAN_START_ERROR = "An error occurred during scan initiation: ";
    public static final String SCAN_STOP_ERROR = "An error has occurred while attempting to stop the scan.: ";
    public static final String SCAN_BASELINE_COMPLETED = "Baseline established successfully!\nDo you wish to see the details?";
    public static final String SCAN_COMPLETED_DIFFS = "Scan completed with differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";
    public static final String SCAN_COMPLETED_NO_DIFFS = "Scan completed with no differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";
    public static final String SCAN_NOT_COMPLETED = "Scan not completed.";
    public static final String SCAN_ESTABLISHING_BASELINE = "Establishing baseline for directory: ";
    public static final String SCAN_DIFFS_PREFIX = "Diffs: ";
    public static final String SCAN_LIVE_FEED_ERROR_PREFIX = "An error occurred while retrieving the live feed: ";
    public static final String SCAN_SHOW_RESULTS_ERROR_PREFIX = "Error displaying scan results: ";

    // Monitored directory popup related strings
    public static final String MON_DIR_POPUP_START_SCAN = "Scan this directory";
    public static final String MON_DIR_POPUP_ESTABLISH_BASELINE = "Establish baseline";
    public static final String MON_DIR_POPUP_RESET_BASELINE = "Reset baseline";
    public static final String MON_DIR_POPUP_RESET_BASELINE_POPUP_TITLE = "Confirm Reset Baseline";
    public static final String MON_DIR_POPUP_RESET_BASELINE_POPUP_MESSAGE = "Type the code to confirm baseline reset for:\n";
    public static final String MON_DIR_POPUP_RESET_BASELINE_SUCCESS = "Baseline reset successfully.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR = "Failed to reset baseline.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR_INVALID = "Invalid code. Baseline reset cancelled.";
    public static final String MON_DIR_POPUP_RESET_BASELINE_ERROR_PREFIX = "Error resetting baseline: ";
    public static final String MON_DIR_TOGGLE_ACTIVE_ENABLE = "Set directory as active";
    public static final String MON_DIR_TOGGLE_ACTIVE_DISABLE = "Set directory as inactive";
    public static final String MON_DIR_TOGGLE_ACTIVE_SUCCESS = "Directory status updated successfully.";
    public static final String MON_DIR_TOGGLE_ACTIVE_ERROR = "Failed to update directory status.";
    public static final String MON_DIR_TOGGLE_INCLUDE_SUBDIR_ENABLE = "Include subdirectories";
    public static final String MON_DIR_TOGGLE_INCLUDE_SUBDIR_DISABLE = "Exclude subdirectories";
    public static final String MON_DIR_TOGGLE_INCLUDE_SUBDIR_SUCCESS = "Subdirectory inclusion status updated successfully.";
    public static final String MON_DIR_TOGGLE_INCLUDE_SUBDIR_ERROR = "Failed to update subdirectory inclusion status.";

    // Show note related strings
    public static final String MON_DIR_POPUP_SHOW_NOTE = "Show note";
    public static final String MON_DIR_POPUP_NO_NOTE = "No note available for this directory.";

    // Update note related strings
    public static final String MON_DIR_POPUP_UPDATE_NOTE = "Update note";
    public static final String MON_DIR_POPUP_UPDATE_NOTE_POPUP_PREFIX = "Update Note -";
    public static final String MON_DIR_POPUP_UPDATE_NOTE_SUCCESS = "Note updated successfully.";
    public static final String MON_DIR_POPUP_UPDATE_NOTE_ERROR = "Failed to update note.";

    // Restore note related strings
    public static final String MON_DIR_POPUP_RESTORE_NOTE = "Restore Note";
    public static final String MON_DIR_POPUP_RESTORE_NOTE_POPUP_PREFIX = "Select which previous note to restore for directory:\n";
    public static final String MON_DIR_POPUP_RESTORE_NO_NOTE_FALLBACK = "No previous notes available to restore for directory:\n";
    public static final String MON_DIR_POPUP_RESTORE_NOTE_PREV = "Restore previous note";
    public static final String MON_DIR_POPUP_INFO_PREV_NOTE = "Previous note: ";
    public static final String MON_DIR_POPUP_INFO_PREV_PREV_NOTE = "Before previous note: ";
    public static final String MON_DIR_POPUP_RESTORE_NOTE_PREV_PREV = "Restore note before previous";
    public static final String MON_DIR_POPUP_RESTORE_NOTE_SUCCESS = "Note restored successfully.";
    public static final String MON_DIR_POPUP_RESTORE_NOTE_ERROR = "Failed to restore note.";

    // New directory screen related strings
    public static final String NEW_DIR_SUCCESS_TEXT = "Directory added successfully!";
    public static final String NEW_DIR_ERROR_PREFIX = "Failed to add directory: ";
    public static final String NEW_DIR_NO_PATH_SELECTED = "No directory selected.";

    // File search related strings
    public static final String FILE_SEARCH_RESULTS_PREFIX = "Results: ";
}
