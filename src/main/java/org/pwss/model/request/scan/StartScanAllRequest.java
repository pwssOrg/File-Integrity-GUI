package org.pwss.model.request.scan;

/**
 * Represents a request to start a scan for all files.
 *
 * @param maxHashExtractionFileSize The maximum file size (in bytes) for which hash extraction should be performed.
 */
public record StartScanAllRequest(long maxHashExtractionFileSize) {
}
