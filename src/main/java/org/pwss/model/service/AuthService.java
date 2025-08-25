package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pwss.exception.LoginFailedException;
import org.pwss.exception.UserExistsLookupException;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.PwssHttpClient;
import org.pwss.model.service.network.util.HttpUtility;
import org.pwss.model.service.request.user.CreateUserRequest;
import org.pwss.model.service.request.user.LoginUserRequest;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * The `AuthService` class provides methods for user authentication, specifically for logging in.
 */
public class AuthService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthService() {
    }

    /**
     * Checks if a user exists by sending a request to the USER_EXISTS endpoint.
     *
     * @return `true` if the user exists (response body indicates existence), otherwise `false`.
     * @throws ExecutionException If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException If the thread executing the request is interrupted.
     * @throws UserExistsLookupException If the request fails or the HTTP status indicates an error.
     */
    public boolean userExists() throws ExecutionException, InterruptedException, UserExistsLookupException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.USER_EXISTS, null);
        if (HttpUtility.responseIsSuccess(response.statusCode())) {
            return Boolean.parseBoolean(response.body());
        } else {
            throw new UserExistsLookupException("Failed to check if user exists. HTTP status: " + response.statusCode());
        }
    }

    /**
     * Authenticates a user by sending their credentials to the LOGIN endpoint.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return `true` if the login request is successful (HTTP status indicates success), otherwise `false`.
     * @throws JsonProcessingException If an error occurs while serializing the login request to JSON.
     */
    public boolean login(String username, String password) throws JsonProcessingException, LoginFailedException, ExecutionException, InterruptedException {
            final String body = objectMapper.writeValueAsString(new LoginUserRequest(username, password));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.LOGIN, body);
        return HttpUtility.responseIsSuccess(response.statusCode());
    }

    /**
     * Creates a new user by sending their credentials to the CREATE_USER endpoint.
     *
     * @param username The username of the user to be created.
     * @param password The password of the user to be created.
     * @return `true` if the user creation request is successful (HTTP status indicates success), otherwise `false`.
     * @throws JsonProcessingException If an error occurs while serializing the user creation request to JSON.
     * @throws ExecutionException If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException If the thread executing the request is interrupted.
     */
    public boolean createUser(String username, String password) throws JsonProcessingException, ExecutionException, InterruptedException {
            final String body = objectMapper.writeValueAsString(new CreateUserRequest(username, password));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.CREATE_USER, body);
        return HttpUtility.responseIsSuccess(response.statusCode());
    }
}
