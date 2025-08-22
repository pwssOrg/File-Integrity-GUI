package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pwss.exception.LoginFailedException;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.PwssHttpClient;
import org.pwss.model.service.network.util.HttpUtility;
import org.pwss.model.service.request.LoginRequest;

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
     * Authenticates a user by sending their credentials to the LOGIN endpoint.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return `true` if the login request is successful (HTTP status indicates success), otherwise `false`.
     * @throws JsonProcessingException If an error occurs while serializing the login request to JSON.
     */
    public boolean login(String username, String password) throws JsonProcessingException, LoginFailedException, ExecutionException, InterruptedException {
            final String body = objectMapper.writeValueAsString(new LoginRequest(username, password));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.LOGIN, body);
        return HttpUtility.responseIsSuccess(response.statusCode());
    }
}
