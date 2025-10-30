package org.pwss.model.request.scan_summary;

/**
 * Request to get the summary for a specific scan by its ID.
 *
 * @param scanId The ID of the scan to retrieve the summary for.
 */
public record GetSummaryForScanRequest(long scanId) {
}
