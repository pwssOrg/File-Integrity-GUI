package org.pwss.model.entity;

import java.util.Date;

/**
 * Represents a directory that is being monitored for changes.
 *
 * @param id                    Unique identifier for the monitored directory.
 * @param path                  Filesystem path of the monitored directory.
 * @param isActive              Indicates if monitoring is currently active for this directory.
 * @param addedAt               Timestamp when the directory was added for monitoring.
 * @param lastScanned           Timestamp of the last scan performed on this directory.
 * @param notes                 Additional notes or comments about the monitored directory.
 * @param baselineEstablished   Indicates if a baseline has been established for change detection.
 * @param includeSubdirectories Indicates if subdirectories should be included in monitoring.
 */
public record MonitoredDirectory(long id, String path, boolean isActive, Date addedAt, Date lastScanned, String notes,
                                 boolean baselineEstablished, boolean includeSubdirectories) {
}
