package org.pwss.model.service.request.monitored_directory;

/**
 * Request to create a new baseline for a monitored directory.
 *
 * @param directoryId  the ID of the monitored directory
 * @param endpointCode the code of the endpoint where the directory is located
 */
public record NewBaselineRequest(long directoryId, long endpointCode) {
}
