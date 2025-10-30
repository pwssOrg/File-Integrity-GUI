package org.pwss.exception.scan;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while retrieving scan differences.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of fetching or analyzing differences between scans, such
 * as invalid input,
 * failed queries, or other related problems.
 */
public final class GetScanDiffsException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a GetScanDiffsException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetScanDiffsException(String message) {
        super(message);
    }
}
