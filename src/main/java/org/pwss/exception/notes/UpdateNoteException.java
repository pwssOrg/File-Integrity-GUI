package org.pwss.exception.notes;

import java.io.Serial;

public final class UpdateNoteException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a `UpdateNoteException` with no detail message or cause.
     */
    public UpdateNoteException() {
        super();
    }

    /**
     * Constructs a `UpdateNoteException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public UpdateNoteException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `UpdateNoteException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public UpdateNoteException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `UpdateNoteException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public UpdateNoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
