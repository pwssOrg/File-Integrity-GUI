package org.pwss.exception.scan;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while retrieving or processing scan
 * status.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of fetching or analyzing scan status information, such as
 * invalid input,
 * failed queries, or other related problems.
 */
public final class ScanStatusException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a ScanStatusException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public ScanStatusException(String message) {
        super(message);
    }
}
