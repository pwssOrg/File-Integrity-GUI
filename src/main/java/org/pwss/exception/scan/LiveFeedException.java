package org.pwss.exception.scan;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while processing live feed data.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of retrieving or analyzing live feed information, such as
 * invalid input,
 * failed connections, or other related problems.
 */
public final class LiveFeedException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a LiveFeedException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public LiveFeedException(String message) {
        super(message);
    }
}
