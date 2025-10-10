package org.pwss.exception.notes;

import java.io.Serial;

public final class RestoreNoteException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a `RestoreNoteException` with no detail message or cause.
     */
    public RestoreNoteException() {
        super();
    }

    /**
     * Constructs a `RestoreNoteException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public RestoreNoteException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `RestoreNoteException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public RestoreNoteException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `RestoreNoteException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public RestoreNoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
