package org.pwss.exception.scan_summary;

public final class GetMostRecentSummaryException extends Exception {

    /**
     * Constructs a `GetMostRecentSummaryException` with no detail message or cause.
     */
    public GetMostRecentSummaryException() {
        super();
    }

    /**
     * Constructs a `GetMostRecentSummaryException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetMostRecentSummaryException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetMostRecentSummaryException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetMostRecentSummaryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetMostRecentSummaryException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetMostRecentSummaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
