package org.pwss.exception.scan;

public final class LiveFeedException extends Exception {

    /**
     * Constructs a `LiveFeedException` with no detail message or cause.
     */
    public LiveFeedException() {
        super();
    }

    /**
     * Constructs a `LiveFeedException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public LiveFeedException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `LiveFeedException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public LiveFeedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `LiveFeedException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public LiveFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
