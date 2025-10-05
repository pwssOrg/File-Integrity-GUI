package org.pwss.exception.monitored_directory;

import java.io.Serial;

public final class NewMonitoredDirectoryException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a `NewMonitoredDirectoryException` with no detail message or cause.
     */
    public NewMonitoredDirectoryException() {
        super();
    }

    /**
     * Constructs a `NewMonitoredDirectoryException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public NewMonitoredDirectoryException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `NewMonitoredDirectoryException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public NewMonitoredDirectoryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `NewMonitoredDirectoryException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public NewMonitoredDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a `NewMonitoredDirectoryException` with the specified detail message and body.
     * The message is appended with " \nPWSS-FE @Exception\n" followed by the body.
     *
     * @param message The detail message to be included in the exception.
     * @param body    Additional information to be included in the exception.
     */
    public NewMonitoredDirectoryException(String message, String body) {
        super(message + " \nPWSS-FE @Exception\n" + body);
    }
}
