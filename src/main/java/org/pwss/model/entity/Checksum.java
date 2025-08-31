package org.pwss.model.entity;

/**
 * Represents a checksum entity with various checksum algorithms.
 *
 * @param id              the unique identifier of the checksum
 * @param file            the associated file entity
 * @param checksumSha256  the SHA-256 checksum value
 * @param checksumSha3    the SHA-3 checksum value
 * @param checksumBlake2b the BLAKE2b checksum value
 */
public record Checksum(long id, File file, String checksumSha256, String checksumSha3, String checksumBlake2b) {
}
