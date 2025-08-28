package org.pwss.model.service.request.monitored_directory;

public record UpdateDirectoryRequest(long id, boolean isActive, String notes, boolean includeSubDirs) {
}
