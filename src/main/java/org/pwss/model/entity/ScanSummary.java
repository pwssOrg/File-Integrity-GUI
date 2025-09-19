package org.pwss.model.entity;

/**
 * A summary of a scan, including its ID, associated scan, file, and checksum.
 *
 * @param id       the unique identifier of the scan summary
 * @param scan     the associated scan entity
 * @param file     the associated file entity
 * @param monitoredDirectory the associated monitored directory entity
 * @param checksum the associated checksum entity
 */
public record ScanSummary(long id, Scan scan, File file, MonitoredDirectory monitoredDirectory, Checksum checksum) {
}
