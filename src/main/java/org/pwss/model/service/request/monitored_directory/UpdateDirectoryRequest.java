package org.pwss.model.service.request.monitored_directory;

/**
 * Request record for updating a monitored directory's details.
 *
 * @param id             the ID of the monitored directory to update
 * @param isActive       the new active status of the directory
 * @param notes          any notes associated with the directory
 * @param includeSubDirs whether to include subdirectories in monitoring
 */
public record UpdateDirectoryRequest(long id, boolean isActive, String notes, boolean includeSubDirs) {
}
