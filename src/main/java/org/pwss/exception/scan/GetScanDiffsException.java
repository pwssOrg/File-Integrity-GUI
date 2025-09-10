package org.pwss.exception.scan;

public final class GetScanDiffsException extends Exception {

    /**
     * Constructs a `GetScanDiffsException` with no detail message or cause.
     */
    public GetScanDiffsException() {
        super();
    }

    /**
     * Constructs a `GetScanDiffsException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetScanDiffsException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetScanDiffsException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetScanDiffsException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetScanDiffsException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetScanDiffsException(String message, Throwable cause) {
        super(message, cause);
    }
}
