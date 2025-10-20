package org.pwss.model.request.file;

/**
 * Request to unquarantine a file.
 *
 * @param keyPath the key path of the file to be unquarantined
 */
public record UnquarantineRequest(String keyPath) {
}
