package org.pwss.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.pwss.model.entity.MonitoredDirectory;
import org.slf4j.Logger;

public final class MonitoredDirectoryUtils {

    // Logger instance for logging purposes
    private final Logger log;

    // Private constructor to prevent instantiation
    private MonitoredDirectoryUtils() {
        this.log =  org.slf4j.LoggerFactory.getLogger(MonitoredDirectoryUtils.class);
    }

    public static String getMonitoredDirectoryNotificationMessage(List<MonitoredDirectory> dirs) {
        if (dirs == null || dirs.isEmpty()) {
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
        if (dir == null || dir.lastScanned() == null) {
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
     * @return true if more than 1 minute has passed since last scan, false otherwise
     */
    public static boolean isScanOlderThan1Minute(MonitoredDirectory dir) {
        if (dir == null || dir.lastScanned() == null) {
            return true; // treat null as "needs scan"
        }

        Instant lastScan = dir.lastScanned().toInstant();
        Instant oneMinuteAgo = Instant.now().minus(Duration.ofMinutes(1));

        return lastScan.isBefore(oneMinuteAgo);
    }
}
