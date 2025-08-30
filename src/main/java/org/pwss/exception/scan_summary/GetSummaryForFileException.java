package org.pwss.exception.scan_summary;

public final class GetSummaryForFileException extends Exception {

    /**
     * Constructs a `GetSummaryForFileException` with no detail message or cause.
     */
    public GetSummaryForFileException() {
        super();
    }

    /**
     * Constructs a `GetSummaryForFileException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetSummaryForFileException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetSummaryForFileException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetSummaryForFileException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetSummaryForFileException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetSummaryForFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
