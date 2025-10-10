package org.pwss.model.response;

/**
 * Represents the response for a live feed request, including the scan status and live feed data.
 *
 * @param isScanRunning indicates whether a scan is currently running
 * @param livefeed      the live feed data as a string
 */
public record LiveFeedResponse(boolean isScanRunning, String livefeed) {
}
