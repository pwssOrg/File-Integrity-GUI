package org.pwss.model.entity;


/**
 * Represents a scan operation performed on a monitored directory.
 *
 * @param id                 Unique identifier for the scan.
 * @param scanTime          Timestamp when the scan was performed.
 * @param status            Status of the scan (e.g., "COMPLETED", "FAILED").
 * @param notes             Additional notes or comments about the scan.
 * @param monitoredDirectory The monitored directory associated with this scan.
 * @param isBaselineScan    Indicates if this scan is a baseline scan.
 */
public record Scan(long id, Time scanTime, String status, Notes notes, MonitoredDirectory monitoredDirectory,
                   boolean isBaselineScan) {
}
