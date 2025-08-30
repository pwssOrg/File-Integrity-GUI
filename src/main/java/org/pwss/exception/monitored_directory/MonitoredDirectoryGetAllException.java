package org.pwss.exception.monitored_directory;

public final class MonitoredDirectoryGetAllException extends Exception {

    /**
     * Constructs a `MonitoredDirectoryGetAllException` with no detail message or cause.
     */
    public MonitoredDirectoryGetAllException() {
        super();
    }

    /**
     * Constructs a `MonitoredDirectoryGetAllException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public MonitoredDirectoryGetAllException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `MonitoredDirectoryGetAllException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public MonitoredDirectoryGetAllException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `MonitoredDirectoryGetAllException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public MonitoredDirectoryGetAllException(String message, Throwable cause) {
        super(message, cause);
    }
}
