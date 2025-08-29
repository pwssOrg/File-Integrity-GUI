package org.pwss.exception.scan;

public final class ScanStatusException extends Exception {

    /**
     * Constructs a `ScanStatusException` with no detail message or cause.
     */
    public ScanStatusException() {
        super();
    }

    /**
     * Constructs a `ScanStatusException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public ScanStatusException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `ScanStatusException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public ScanStatusException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `ScanStatusException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public ScanStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
