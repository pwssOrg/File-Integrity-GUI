package org.pwss.exception.user;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when a user lookup operation detects that the user already
 * exists in the system.
 * This exception extends PWSSbaseException and is used to indicate issues
 * specific to duplicate user detection
 * during lookup operations.
 */
public final class UserExistsLookupException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a UserExistsLookupException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public UserExistsLookupException(String message) {
        super(message);
    }
}
