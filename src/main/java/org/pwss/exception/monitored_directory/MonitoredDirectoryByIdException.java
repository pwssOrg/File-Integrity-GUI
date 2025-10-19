package org.pwss.exception.monitored_directory;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while retrieving a monitored directory
 * by its ID.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of fetching a monitored directory by ID, such as invalid
 * input,
 * failed commands, or other related problems.
 */
public final class MonitoredDirectoryByIdException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a MonitoredDirectoryByIdException with the specified detail
     * message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public MonitoredDirectoryByIdException(String message) {
        super(message);
    }
}
