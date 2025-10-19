package org.pwss.exception.user;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when a login operation fails due to various reasons such as
 * invalid credentials or other authentication issues.
 * This exception extends PWSSbaseException and is used to indicate specific
 * problems during the login process.
 */
public final class LoginException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a LoginFailedException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public LoginException(String message) {
        super(message);
    }
}
