package org.pwss.exception.start;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown to indicate that the application failed to launch.
 * This exception is used to signal errors during the startup phase of the
 * application,
 * typically caused by critical initialization failures, configuration issues,
 * or missing dependencies.
 */
public final class FailedToLaunchAppException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a FailedToLaunchAppException with the specified detail message.
     * The message is appended with "\nPWSS-FE @Exception" to provide consistent
     * formatting for error reporting.
     *
     * @param message The detail message to be included in the exception. Can be
     *                null.
     */
    public FailedToLaunchAppException(String message) {
        super(message);
    }

    /**
     * Constructs a FailedToLaunchAppException with the specified cause.
     * The detail message is set to the string representation of the cause, or to
     * "null" if the cause is null.
     *
     * @param cause The cause of the exception. A null value is permitted, and
     *              indicates that the cause is
     *              unknown.
     */
    public FailedToLaunchAppException(Throwable cause) {
        super(cause);
    }
}