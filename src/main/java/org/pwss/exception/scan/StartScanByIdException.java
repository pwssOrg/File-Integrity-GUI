package org.pwss.exception.scan;

public class StartScanByIdException extends Exception {

    /**
     * Constructs a `StartScanByIdException` with no detail message or cause.
     */
    public StartScanByIdException() {
        super();
    }

    /**
     * Constructs a `StartScanByIdException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public StartScanByIdException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `StartScanByIdException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public StartScanByIdException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `StartScanByIdException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public StartScanByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
