package org.pwss.model.request.file;

/**
 * Request to quarantine a file by its ID.
 *
 * @param fileId the ID of the file to be quarantined
 */
public record QuarantineRequest(long fileId) {
}
