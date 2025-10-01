package org.pwss.exception.scan_summary;

public final class GetSearchFilesException extends Exception {

    /**
     * Constructs a `GetSearchFilesException` with no detail message or cause.
     */
    public GetSearchFilesException() {
        super();
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetSearchFilesException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetSearchFilesException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetSearchFilesException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetSearchFilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
