package org.pwss.exception;

public class LoginFailedException extends Exception{

    /**
     * Constructs a `LoginFailedException` with no detail message or cause.
     */
    public LoginFailedException() {
        super();
    }

    /**
     * Constructs a `LoginFailedException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public LoginFailedException(String message) {
        super(message + " \nPWSS @Exception");
    }

    /**
     * Constructs a `LoginFailedException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `LoginFailedException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
