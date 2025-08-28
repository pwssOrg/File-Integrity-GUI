package org.pwss.exception;

public final class UserExistsLookupException extends Exception {

    /**
     * Constructs a `UserExistsLookupException` with no detail message or cause.
     */
    public UserExistsLookupException() {
        super();
    }

    /**
     * Constructs a `UserExistsLookupException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public UserExistsLookupException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `UserExistsLookupException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public UserExistsLookupException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `UserExistsLookupException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public UserExistsLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
