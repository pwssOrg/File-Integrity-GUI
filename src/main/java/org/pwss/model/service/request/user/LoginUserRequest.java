package org.pwss.model.service.request.user;

/**
 * Request to log in a user.
 *
 * @param username the username of the user
 * @param password the password of the user
 */
public record LoginUserRequest(String username, String password) {
}
