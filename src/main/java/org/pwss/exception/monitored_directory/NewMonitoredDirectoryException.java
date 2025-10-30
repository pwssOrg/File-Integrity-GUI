package org.pwss.exception.monitored_directory;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while creating a new monitored
 * directory.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of adding a new monitored directory, such as invalid
 * input,
 * failed commands, or other related problems.
 */
public final class NewMonitoredDirectoryException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a NewMonitoredDirectoryException with the specified detail
     * message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public NewMonitoredDirectoryException(String message) {
        super(message);
    }

    /**
     * Constructs a NewMonitoredDirectoryException with the specified detail message
     * and body.
     * The message is appended with "\nPWSS-FE @Exception\n" followed by the body.
     *
     * @param message The detail message to be included in the exception.
     * @param body    Additional information to be included in the exception.
     */
    public NewMonitoredDirectoryException(String message, String body) {
        super(message + "\nBody: " + body);
    }
}
