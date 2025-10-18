package org.pwss.model.request.user;

/**
 * Request to log in a user.
 *
 * @param username the username of the user
 * @param password the password of the user
 * @param licenseKey the license key associated with the user
 */
public record LoginUserRequest(String username, String password, String licenseKey) {
}
