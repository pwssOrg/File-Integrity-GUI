package org.pwss.exception.file;

import java.io.Serial;
import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown to indicate an error occurred during the unquarantine of a
 * file.
 *
 * This exception provides specific information about the error that occurred
 * during the file unquarantine
 * process, allowing developers to handle such errors appropriately within
 * their applications.
 */
public final class UnquarantineFileException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnquarantineFileException with the specified detail
     * message.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public UnquarantineFileException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnquarantineFileException with the specified detail
     * message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the
     *                getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the
     *                getCause() method). A null value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.
     */
    public UnquarantineFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
