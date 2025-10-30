package org.pwss.exception.notes;

import java.io.Serial;

import org.pwss.exception.PWSSbaseException;

/**
 * Exception thrown when an error occurs while updating a note.
 * This exception extends PWSSbaseException and is used to indicate specific
 * issues encountered
 * during the process of updating a note, such as invalid input,
 * failed commands, or other related problems.
 */
public final class UpdateNoteException extends PWSSbaseException {

    /**
     * The serial version UID for object serialization. It ensures compatibility
     * between different versions
     * of this exception class when deserializing objects.
     */
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructs a UpdateNoteException with the specified detail message.
     * The message is appended with "\nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public UpdateNoteException(String message) {
        super(message);
    }
}
