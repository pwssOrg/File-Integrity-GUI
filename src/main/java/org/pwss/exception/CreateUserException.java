package org.pwss.exception;

public class CreateUserException extends Exception {

    /**
     * Constructs a `CreateUserException` with no detail message or cause.
     */
    public CreateUserException() {
        super();
    }

    /**
     * Constructs a `CreateUserException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public CreateUserException(String message) {
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a `CreateUserException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public CreateUserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `CreateUserException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public CreateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
