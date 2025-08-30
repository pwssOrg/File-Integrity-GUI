package org.pwss.model.service.request.user;

/**
 * Request to create a new user.
 *
 * @param username the username of the new user
 * @param password the password of the new user
 */
public record CreateUserRequest(String username, String password) {
}
