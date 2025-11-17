package org.pwss.exception.scan;

import java.io.Serial;
import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when there is an error while getting the difference count
 * during a scan operation.
 */
public class GetDiffCountException extends PWSSbaseException {
    /**
     * Serial version UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new GetDiffCountException with the specified detail message.
     *
     * @param message the detail message.
     */
    public GetDiffCountException(String message) {
        super(message);
    }
}
