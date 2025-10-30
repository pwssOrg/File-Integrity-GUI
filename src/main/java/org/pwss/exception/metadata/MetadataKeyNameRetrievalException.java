package org.pwss.exception.metadata;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when there is a failure in retrieving the key name from a
 * quarantined metadata file.
 *
 * This exception provides specific information about the error that occurred
 * during the key name retrieval
 * process,
 * allowing developers to handle such errors appropriately within their
 * applications.
 */
public final class MetadataKeyNameRetrievalException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a new MetadataKeyNameRetrievalException with the specified detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public MetadataKeyNameRetrievalException(String message) {
        super(message);
    }

    /**
     * Constructs a new MetadataKeyNameRetrievalException with the specified detail
     * message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                getCause() method). A null value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.
     */
    public MetadataKeyNameRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}