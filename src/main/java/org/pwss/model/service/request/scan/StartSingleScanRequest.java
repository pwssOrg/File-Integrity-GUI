package org.pwss.model.service.request.scan;

/**
 * Request to start a single scan by a monitored directory's ID.
 *
 * @param id The ID of the monitored directory to scan.
 */
public record StartSingleScanRequest(long id) {
}
