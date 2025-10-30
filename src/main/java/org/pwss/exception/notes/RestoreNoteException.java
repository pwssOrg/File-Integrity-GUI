package org.pwss.exception.notes;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while restoring a note.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of restoring a note, such as invalid input,
 * failed commands, or other related problems.
 */
public final class RestoreNoteException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a RestoreNoteException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public RestoreNoteException(String message) {
        super(message);
    }
}
