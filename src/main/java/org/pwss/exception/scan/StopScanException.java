package org.pwss.exception.scan;

public final class StopScanException extends Exception {

    /**
     * Constructs a `StopScanException` with no detail message or cause.
     */
    public StopScanException() {
        super();
    }

    /**
     * Constructs a `StopScanException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public StopScanException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `StopScanException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public StopScanException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `StopScanException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public StopScanException(String message, Throwable cause) {
        super(message, cause);
    }
}
