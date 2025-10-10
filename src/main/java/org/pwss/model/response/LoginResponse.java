package org.pwss.model.response;

/**
 * Response indicating the result of a login attempt.
 *
 * @param successful true if the login was successful, false otherwise
 */
public record LoginResponse(boolean successful) {
}
