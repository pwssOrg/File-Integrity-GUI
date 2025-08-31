package org.pwss.exception.monitored_directory;

public final class MonitoredDirectoryByIdException extends Exception {

    /**
     * Constructs a `MonitoredDirectoryByIdException` with no detail message or cause.
     */
    public MonitoredDirectoryByIdException() {
        super();
    }

    /**
     * Constructs a `MonitoredDirectoryByIdException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public MonitoredDirectoryByIdException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `MonitoredDirectoryByIdException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public MonitoredDirectoryByIdException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `MonitoredDirectoryByIdException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public MonitoredDirectoryByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
