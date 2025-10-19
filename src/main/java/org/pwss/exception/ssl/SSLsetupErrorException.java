package org.pwss.exception.ssl;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs during the setup of SSL/TLS
 * configuration.
 * This exception extends PWSSbaseException and is used to indicate issues
 * specific to SSL/TLS setup
 * in the system, such as invalid certificate files, incorrect configuration
 * settings, or other related problems.
 */
public final class SSLsetupErrorException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a SSLsetupErrorException with the specified detail message.
     * The message is appended with "\nPWSS-FE @Exception" to provide consistent
     * formatting for error reporting.
     *
     * @param message The detail message to be included in the exception. Can be
     *                null.
     */
    public SSLsetupErrorException(String message) {
        super(message);
    }

    /**
     * Constructs a SSLsetupErrorException with the specified detail message
     * and cause.
     *
     * @param message The detail message to be included in the exception. Can be
     *                null.
     * @param cause   The cause of the exception. A null value is permitted, and
     *                indicates that the cause is
     *                unknown.
     */
    public SSLsetupErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}