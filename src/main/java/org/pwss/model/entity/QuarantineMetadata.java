package org.pwss.model.entity;

/**
 * A record that represents metadata for a quarantined file.
 *
 * @param fileId  The unique identifier of the file.
 * @param keyName The unique key name associated with the quarantined file.
 */
public record QuarantineMetadata(long fileId, String keyName) {
}
