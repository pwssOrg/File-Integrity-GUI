package org.pwss.exception.metadata;

import java.io.Serial;

/**
 * Exception thrown when there is a failure in removing metadata for a
 * quarantined file.
 *
 * This exception provides specific information about the error that occurred
 * during the metadata removal process,
 * allowing developers to handle such errors appropriately within their
 * applications.
 */
public final class MetadataRemoveException extends Exception {

     /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Constructs a new MetadataRemoveException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public MetadataRemoveException(String message) {
        super(message);
    }

    /**
     * Constructs a new MetadataRemoveException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                getCause() method). A null value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.
     */
    public MetadataRemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new MetadataRemoveException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval by the getCause()
     *              method). A null value
     *              is permitted, and indicates that the cause is nonexistent or
     *              unknown.
     */
    public MetadataRemoveException(Throwable cause) {
        super(cause);
    }
}
