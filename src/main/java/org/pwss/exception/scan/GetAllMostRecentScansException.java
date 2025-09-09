package org.pwss.exception.scan;

public final class GetAllMostRecentScansException extends Exception {

    /**
     * Constructs a `GetAllMostRecentScansException` with no detail message or cause.
     */
    public GetAllMostRecentScansException() {
        super();
    }

    /**
     * Constructs a `GetAllMostRecentScansException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetAllMostRecentScansException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetAllMostRecentScansException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetAllMostRecentScansException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetAllMostRecentScansException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetAllMostRecentScansException(String message, Throwable cause) {
        super(message, cause);
    }
}
