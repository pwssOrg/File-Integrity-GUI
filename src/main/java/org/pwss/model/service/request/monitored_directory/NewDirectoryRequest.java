package org.pwss.model.service.request.monitored_directory;

/**
 * Request to add a new directory to be monitored.
 *
 * @param path                  The path of the directory to be monitored.
 * @param includeSubdirectories Whether to include subdirectories in the monitoring.
 * @param isActive              Whether the directory monitoring is active.
 */
public record NewDirectoryRequest(String path, boolean includeSubdirectories, boolean isActive) {
}
