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

public class AuthService {
    private final PwssHttpClient httpClient = new PwssHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<Boolean> login(String username, String password) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            final String body = objectMapper.writeValueAsString(new LoginRequest(username, password));
            return httpClient.request(Endpoint.LOGIN, body, headers)
                    .thenApply(response -> {
                        // TODO: parse response properly (e.g. JSON)
                        return response.contains("success"); // placeholder
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
