package org.pwss.model.response;

/**
 * Represents the response after attempting to quarantine a file.
 *
 * @param success Indicates whether the quarantine operation was successful.
 * @param keyName The name of the key associated with the quarantined file.
 */
public record QuarantineResponse(boolean success, String keyName) {
}
