package org.pwss.exception.scan;

public class StartScanAllException extends Exception {

    /**
     * Constructs a `StartScanAllException` with no detail message or cause.
     */
    public StartScanAllException() {
        super();
    }

    /**
     * Constructs a `StartScanAllException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public StartScanAllException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `StartScanAllException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public StartScanAllException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `StartScanAllException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public StartScanAllException(String message, Throwable cause) {
        super(message, cause);
    }
}
