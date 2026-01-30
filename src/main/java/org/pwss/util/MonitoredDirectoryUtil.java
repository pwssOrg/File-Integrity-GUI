package org.pwss.util;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import org.pwss.model.entity.MonitoredDirectory;
import org.slf4j.Logger;

/**
 * Utility class for handling operations related to monitored directories.
 */
public final class MonitoredDirectoryUtil {

    /**
     * Logger instance for logging purposes
     */
    private final static Logger log = org.slf4j.LoggerFactory.getLogger(MonitoredDirectoryUtil.class);

    /**
     * Enumeration representing the notification status of a monitored directory
     * based on its last scan time.
     */
    public enum DirNotificationStatus {
        UP_TO_DATE,
        NEVER_SCANNED,
        NO_BASELINE,
        WEEK_OLD,
        TWO_WEEKS_OLD,
        MONTH_OLD,
        YEAR_OLD;

        /**
         * Gets the color associated with the notification status.
         *
         * @return Color representing the notification status
         */
        public Color getForegroundColor() {
            return switch (this) {
                case UP_TO_DATE -> Color.GREEN;
                case WEEK_OLD, TWO_WEEKS_OLD -> Color.ORANGE;
                case NEVER_SCANNED, NO_BASELINE, YEAR_OLD, MONTH_OLD -> Color.RED;
            };
        }

        /**
         * Gets the tooltip text associated with the notification status.
         *
         * @return String containing the tooltip text
         */
        public String getTooltipText() {
            return switch (this) {
                case UP_TO_DATE -> "Directory is up to date.";
                case NEVER_SCANNED -> "Directory has never been scanned.";
                case NO_BASELINE -> "Directory has no established baseline.";
                case WEEK_OLD -> "Directory was last scanned over a week ago.";
                case TWO_WEEKS_OLD -> "Directory was last scanned over two weeks ago.";
                case MONTH_OLD -> "Directory was last scanned over a month ago.";
                case YEAR_OLD -> "Directory was last scanned over a year ago.";
            };
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private MonitoredDirectoryUtil() {
    }

    /**
     * Generates a notification message based on the state of monitored directories.
     *
     * @param dirs List of monitored directories to check
     * @return A string containing notifications about the state of each directory,
     *         or an empty string if no
     *         directories are present
     */
    public static String getMonitoredDirectoryNotificationMessage(List<MonitoredDirectory> dirs) {
        if (dirs == null || dirs.isEmpty()) {
            log.debug("Generated notification message: {}", StringConstants.NOTIFICATION_NO_MONITORED_DIRS);
            return StringConstants.NOTIFICATION_NO_MONITORED_DIRS;
        }

        StringBuilder message = new StringBuilder();
        for (MonitoredDirectory dir : dirs) {
            switch (getDirNotificationStatus(dir)) {
                case DirNotificationStatus.NEVER_SCANNED -> message.append(
                        "Directory '").append(dir.path()).append("' has never been scanned.\n");
                case DirNotificationStatus.NO_BASELINE -> message.append(
                        "Directory '").append(dir.path()).append("' has no established baseline.\n");
                case DirNotificationStatus.WEEK_OLD -> message.append(
                        "Directory '").append(dir.path()).append("' was last scanned over a week ago.\n");
                case DirNotificationStatus.TWO_WEEKS_OLD -> message.append(
                        "Directory '").append(dir.path()).append("' was last scanned over two weeks ago.\n");
                case DirNotificationStatus.MONTH_OLD -> message.append(
                        "Directory '").append(dir.path()).append("' was last scanned over a month ago.\n");
                case DirNotificationStatus.YEAR_OLD -> message.append(
                        "Directory '").append(dir.path()).append("' was last scanned over a year ago.\n");
                case DirNotificationStatus.UP_TO_DATE -> {
                    // No notification needed for up-to-date directories
                }
            }
        }
        return message.toString();
    }
    
    /**
     * Determines the scan age status of a monitored directory based on the time
     * since its last scan.
     *
     * @param dir the MonitoredDirectory to check
     * @return the ScanAgeStatus representing the age of the last scan
     */
    public static DirNotificationStatus getDirNotificationStatus(MonitoredDirectory dir) {
        if (!dir.baselineEstablished()) {
            return DirNotificationStatus.NO_BASELINE;
        } else if (dir.lastScanned() == null) {
            return DirNotificationStatus.NEVER_SCANNED;
        }

        Instant lastScan = dir.lastScanned().toInstant();
        Instant now = Instant.now();

        long daysSinceLastScan = Duration.between(lastScan, now).toDays();

        if (daysSinceLastScan > 365) {
            return DirNotificationStatus.YEAR_OLD;
        } else if (daysSinceLastScan > 30) {
            return DirNotificationStatus.MONTH_OLD;
        } else if (daysSinceLastScan > 14) {
            return DirNotificationStatus.TWO_WEEKS_OLD;
        } else if (daysSinceLastScan > 7) {
            return DirNotificationStatus.WEEK_OLD;
        } else {
            return DirNotificationStatus.UP_TO_DATE;
        }
    }

    /**
     * Filters a list of monitored directories based on whether their paths exist in
     * the filesystem.
     *
     * @param inputList The list of monitored directories to be filtered.
     * @return A new list containing only the directories from the input list that
     *         have valid, confirmed paths.
     */
    public static List<MonitoredDirectory> filterMonitoredDirectoriesOnConfirmedPath(
            List<MonitoredDirectory> inputList) {

        List<MonitoredDirectory> mDirectories = new LinkedList<>();

        for (MonitoredDirectory m : inputList) {
            if (Files.exists(Path.of(m.path()))) {
                mDirectories.add(m);
            }
        }
        return mDirectories;
    }
}
