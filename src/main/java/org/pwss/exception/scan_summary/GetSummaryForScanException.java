package org.pwss.exception.scan_summary;

public final class GetSummaryForScanException extends Exception {

    /**
     * Constructs a `GetSummaryForScanException` with no detail message or cause.
     */
    public GetSummaryForScanException() {
        super();
    }

    /**
     * Constructs a `GetSummaryForScanException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public GetSummaryForScanException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `GetSummaryForScanException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public GetSummaryForScanException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `GetSummaryForScanException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public GetSummaryForScanException(String message, Throwable cause) {
        super(message, cause);
    }
}
