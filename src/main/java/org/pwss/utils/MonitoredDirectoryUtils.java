package org.pwss.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.pwss.model.entity.MonitoredDirectory;
import org.slf4j.Logger;

/**
 * Utility class for handling operations related to monitored directories.
 */
public final class MonitoredDirectoryUtils {

    /**
     * Logger instance for logging purposes
     */
    private final static Logger log = org.slf4j.LoggerFactory.getLogger(MonitoredDirectoryUtils.class);

    /**
     * Private constructor to prevent instantiation
     */
    private MonitoredDirectoryUtils() {
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
            if (!dir.baselineEstablished()) {
                message.append(StringConstants.NOTIFICATION_NO_BASELINE).append(dir.path()).append("\n");
            } else if (isScanOlderThanAWeek(dir)) {
                message.append(StringConstants.NOTIFICATION_OLD_SCAN).append(dir.path()).append("\n");
            }
        }
        return message.toString();
    }

    /**
     * Checks if the time since the last scan of the given directory
     * is more than one week (7 days).
     *
     * @param dir the MonitoredDirectory to check
     * @return true if more than 7 days have passed since last scan, false otherwise
     */
    public static boolean isScanOlderThanAWeek(MonitoredDirectory dir) {
        if (dir == null) {
            log.error("Can not check if scan is older than a week due to a null error");
            log.debug("Monitored Directory is null in method: isScanOlderThanAWeek");
            return false;
        }

        if (dir.lastScanned() == null) {
            log.debug("Monitored Directory with id {} has not been scanned for 7 days", dir.id());
            return true; // treat null as "needs scan"
        }

        Instant lastScan = dir.lastScanned().toInstant();
        Instant oneWeekAgo = Instant.now().minus(Duration.ofDays(7));

        return lastScan.isBefore(oneWeekAgo);
    }

    /**
     * NOTE: FOR TESTING PURPOSES ONLY
     * Checks if the time since the last scan of the given directory
     * is more than one minute.
     *
     * @param dir the MonitoredDirectory to check
     * @return true if more than 1 minute has passed since last scan, false
     *         otherwise
     */
    public static boolean isScanOlderThan1Minute(MonitoredDirectory dir) {
        if (dir == null) {
            log.error("Can not check if scan is older than a minute due to a null error");
            log.debug("Monitored Directory is null in method: isScanOlderThan1Minute");
            return false;
        }

        if (dir.lastScanned() == null) {
            log.debug("Monitored Directory with id {} has not been scanned for 1 minute", dir.id());
            return true; // treat null as "needs scan"
        }

        Instant lastScan = dir.lastScanned().toInstant();
        Instant oneMinuteAgo = Instant.now().minus(Duration.ofMinutes(1));

        return lastScan.isBefore(oneMinuteAgo);
    }
}
