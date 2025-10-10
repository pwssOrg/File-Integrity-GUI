package org.pwss.model.request.monitored_directory;

/**
 * Request record for retrieving a monitored directory by its ID.
 *
 * @param directoryId the ID of the monitored directory to retrieve
 */
public record GetDirectoryByIdRequest(long directoryId) {
}
