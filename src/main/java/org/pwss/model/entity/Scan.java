package org.pwss.model.entity;

import java.util.Date;

/**
 * Represents a scan operation performed on a monitored directory.
 *
 * @param id                 Unique identifier for the scan.
 * @param scanTime          Timestamp when the scan was performed.
 * @param status            Status of the scan (e.g., "COMPLETED", "FAILED").
 * @param notes             Additional notes or comments about the scan.
 * @param monitoredDirectory The monitored directory associated with this scan.
 */
public record Scan(long id, Date scanTime, String status, String notes, MonitoredDirectory monitoredDirectory) {
}
