package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.PwssHttpClient;



import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

record LoginRequest(String username, String password) {
}
/**
 * Nice Work Stefan
 */
public class AuthService {

    private final PwssHttpClient httpClient = new PwssHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthService() {
    }


    /**
     * This Method is invoking a temp non general method that Stefan will delete once he seen the workflow 
     * {@link org.pwss.model.service.network.PwssHttpClient#tmp_request_remove_later(Endpoint, String)}
     * @param username
     * @param password
     * @return
     */
    public boolean login(String username, String password) {

        try {
            final String body = objectMapper.writeValueAsString(new LoginRequest(username, password));

            return httpClient.tmp_request_remove_later(Endpoint.LOGIN, body);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public CompletableFuture<Boolean> loginAsync(String username, String password) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            final String body = objectMapper.writeValueAsString(new LoginRequest(username, password));
            return httpClient.requestAsync(Endpoint.LOGIN, body, headers)
                    .thenApply(response -> {
                        // TODO: parse response properly (e.g. JSON)
                        return response.contains("success"); // placeholder
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
