package org.pwss.model.request.scan;

/**
 * Request to start a single scan by a monitored directory's ID.
 *
 * @param id The ID of the monitored directory to scan.
 * @param maxHashExtractionFileSize The maximum file size (in bytes) for which hash extraction should be performed.
 */
public record StartSingleScanRequest(long id, long maxHashExtractionFileSize) {
}
