package org.pwss.exception.scan;

public final class GetMostRecentScansException extends Exception {

    /**
     * Constructs a `GetMostRecentScansException` with no detail message or cause.
     */
    public GetMostRecentScansException() {
        super();
    }

    /**
     * Constructs a `GetMostRecentScansException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetMostRecentScansException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetMostRecentScansException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetMostRecentScansException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetMostRecentScansException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetMostRecentScansException(String message, Throwable cause) {
        super(message, cause);
    }
}
