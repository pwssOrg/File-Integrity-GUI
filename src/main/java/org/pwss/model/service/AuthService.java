package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.util.HttpUtility;
import org.pwss.model.service.network.PwssHttpClient;
import org.pwss.model.service.request.LoginRequest;
import java.util.concurrent.CompletableFuture;

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
     * Asynchronously logs in a user by sending their credentials to the login endpoint.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return A `CompletableFuture` that resolves to `true` if the login is successful, otherwise `false`.
     * @throws RuntimeException If there is an error serializing the login request to JSON.
     */
    public CompletableFuture<Boolean> loginAsync(String username, String password) {
        try {
            final String body = objectMapper.writeValueAsString(new LoginRequest(username, password));
            return PwssHttpClient.getInstance().requestAsync(Endpoint.LOGIN, body)
                    .thenApply(response -> HttpUtility.responseIsSuccess(response.statusCode()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
