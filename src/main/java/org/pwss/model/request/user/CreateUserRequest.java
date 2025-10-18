package org.pwss.model.request.user;

/**
 * Request to create a new user.
 *
 * @param username the username of the new user
 * @param password the password of the new user
 * @param licenseKey the license key associated with the new user
 */
public record CreateUserRequest(String username, String password, String licenseKey) {
}
