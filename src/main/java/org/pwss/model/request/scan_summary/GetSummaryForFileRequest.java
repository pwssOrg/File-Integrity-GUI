package org.pwss.model.request.scan_summary;

/**
 * Request to get the scan summary for a specific file by its ID.
 *
 * @param fileId The ID of the file to retrieve the scan summary for.
 */
public record GetSummaryForFileRequest(long fileId) {
}
