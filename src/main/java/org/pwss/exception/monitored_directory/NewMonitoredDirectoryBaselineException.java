package org.pwss.exception.monitored_directory;

public final class NewMonitoredDirectoryBaselineException extends Exception {

    /**
     * Constructs a `NewMonitoredDirectoryBaselineException` with no detail message or cause.
     */
    public NewMonitoredDirectoryBaselineException() {
        super();
    }

    /**
     * Constructs a `NewMonitoredDirectoryBaselineException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public NewMonitoredDirectoryBaselineException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `NewMonitoredDirectoryBaselineException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public NewMonitoredDirectoryBaselineException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `NewMonitoredDirectoryBaselineException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public NewMonitoredDirectoryBaselineException(String message, Throwable cause) {
        super(message, cause);
    }
}
