package org.pwss.exception.monitored_directory;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while creating a new baseline for a
 * monitored directory.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of setting a new baseline, such as invalid input,
 * failed commands, or other related problems.
 */
public final class NewMonitoredDirectoryBaselineException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a NewMonitoredDirectoryBaselineException with the specified detail
     * message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public NewMonitoredDirectoryBaselineException(String message) {
        super(message);
    }
}
