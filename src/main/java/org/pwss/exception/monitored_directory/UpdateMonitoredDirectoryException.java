package org.pwss.exception.monitored_directory;

public final class UpdateMonitoredDirectoryException extends Exception {

    /**
     * Constructs a `UpdateMonitoredDirectoryException` with no detail message or cause.
     */
    public UpdateMonitoredDirectoryException() {
        super();
    }

    /**
     * Constructs a `UpdateMonitoredDirectoryException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public UpdateMonitoredDirectoryException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `UpdateMonitoredDirectoryException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public UpdateMonitoredDirectoryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `UpdateMonitoredDirectoryException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public UpdateMonitoredDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
