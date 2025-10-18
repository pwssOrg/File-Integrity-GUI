package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.user.CreateUserException;
import org.pwss.exception.user.LoginException;
import org.pwss.exception.user.UserExistsLookupException;
import org.pwss.model.request.user.CreateUserRequest;
import org.pwss.model.request.user.LoginUserRequest;
import org.pwss.model.response.LoginResponse;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;

/**
 * The `AuthService` class provides methods for user authentication, specifically for logging in.
 */
public class AuthService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Checks if a user exists by sending a request to the USER_EXISTS endpoint.
     *
     * @return `true` if the user exists, otherwise `false`.
     * @throws UserExistsLookupException If an error occurs while checking for user existence.
     * @throws ExecutionException        If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException      If the thread executing the request is interrupted.
     */
    public boolean userExists() throws UserExistsLookupException, ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.USER_EXISTS, null);

        return switch (response.statusCode()) {
            case 200 -> Boolean.parseBoolean(response.body());
            case 404 -> false;
            case 500 -> throw new UserExistsLookupException("User existence check failed: Server error");
            default ->
                    throw new UserExistsLookupException("User existence check failed: Unexpected status code " + response.statusCode());
        };
    }

    /**
     * Attempts to log in a user by sending their credentials to the LOGIN endpoint.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @param licenseKey The license key associated with the user attempting to log in.
     * @return `true` if the login is successful, otherwise `false`.
     * @throws JsonProcessingException If an error occurs while serializing the login request to JSON.
     * @throws LoginException          If the login attempt fails due to invalid credentials or server error.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     */
    public boolean login(String username, String password, String licenseKey) throws LoginException, JsonProcessingException, ExecutionException, InterruptedException {
        final String body = objectMapper.writeValueAsString(new LoginUserRequest(username, password, licenseKey));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.LOGIN, body);

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), LoginResponse.class).successful();
            case 402 -> throw new LoginException("Login failed: Invalid license key");
            case 404 -> throw new LoginException("Login failed: User not found");
            case 500 -> throw new LoginException("Login failed: Server error during login");
            default -> false;
        };
    }

    /**
     * Creates a new user by sending their credentials to the CREATE_USER endpoint.
     *
     * @param username The username of the user to be created.
     * @param password The password of the user to be created.
     * @param licenseKey The license key associated with the user to be created.
     * @return `true` if the user creation request is successful (HTTP status indicates success), otherwise `false`.
     * @throws JsonProcessingException If an error occurs while serializing the user creation request to JSON.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     */
    public boolean createUser(String username, String password, String licenseKey) throws CreateUserException, JsonProcessingException, ExecutionException, InterruptedException {
        final String body = objectMapper.writeValueAsString(new CreateUserRequest(username, password, licenseKey));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.CREATE_USER, body);

        return switch (response.statusCode()) {
            case 201 -> true;
            case 402 -> throw new CreateUserException("Create user failed: Invalid license key");
            case 409 -> throw new CreateUserException("Create user failed: User already exists");
            case 500 -> throw new CreateUserException("Create user failed: Server error during creation");
            default -> false;
        };
    }
}
