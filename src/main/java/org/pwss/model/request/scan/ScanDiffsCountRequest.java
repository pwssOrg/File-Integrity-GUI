package org.pwss.model.request.scan;

/**
 * Represents a request to count differences in a scan.
 *
 * @param scanId the identifier for the scan to count differences for
 */
public record ScanDiffsCountRequest(long scanId) {
}
