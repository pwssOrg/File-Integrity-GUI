package org.pwss.exception.user;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when a user creation operation fails due to various reasons
 * such as invalid input,
 * duplicate username, or other issues during account registration.
 * This exception extends PWSSbaseException and is used to indicate specific
 * problems encountered while creating a
 * new user.
 */
public final class CreateUserException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a CreateUserException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public CreateUserException(String message) {
        super(message);
    }
}
