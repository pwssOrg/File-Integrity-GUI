package org.pwss.exception.user;

public final class LoginException extends Exception {

    /**
     * Constructs a `LoginFailedException` with no detail message or cause.
     */
    public LoginException() {
        super();
    }

    /**
     * Constructs a `LoginFailedException` with the specified detail message.
     * The message is appended with " \nPWSS @Exception".
     *
     * @param message The detail message to be included in the exception.
     */
    public LoginException(String message) {
        super(message + " \nPWSS-FE @Exception");
    }

    /**
     * Constructs a `LoginFailedException` with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public LoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a `LoginFailedException` with the specified detail message and cause.
     *
     * @param message The detail message to be included in the exception.
     * @param cause   The cause of the exception.
     */
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
