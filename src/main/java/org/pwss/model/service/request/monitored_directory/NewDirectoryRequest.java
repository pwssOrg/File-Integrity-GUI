package org.pwss.model.service.request.monitored_directory;

public record NewDirectoryRequest(String path, boolean includeSubdirectories, boolean isActive) {
}
