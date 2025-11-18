package org.pwss.model.entity;

import java.util.Date;


/**
 * A record class representing a monitored directory.
 * This class stores information about directories that are being monitored,
 * including their ID, path, active status, timestamps, and other relevant details.
 */
public record MonitoredDirectory(
        /**
         * The unique identifier for this monitored directory.
         */
        long id,

        /**
         * The file system path of the monitored directory.
         */
        String path,

        /**
         * A flag indicating whether the directory is currently active (being monitored).
         */
        boolean isActive,

        /**
         * The time when this directory was added to monitoring.
         */
        Time addedAt,

        /**
         * The date and time when this directory was last scanned.
         */
        Date lastScanned,

        /**
         * Additional notes or comments about this monitored directory.
         */
        Notes notes,

        /**
         * A flag indicating whether a baseline has been established for this directory.
         * This might be used to determine if the initial state of the directory has been recorded.
         */
        boolean baselineEstablished,

        /**
         * A flag indicating whether subdirectories should also be monitored
         * when monitoring this directory.
         */
        boolean includeSubdirectories) {
}