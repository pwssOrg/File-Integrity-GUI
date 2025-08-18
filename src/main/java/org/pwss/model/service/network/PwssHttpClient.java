package org.pwss.model.service.network;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PwssHttpClient {
    /**
     * The ObjectMapper instance used for JSON serialization and deserialization.
     * This is typically used to convert Java objects to JSON and vice versa.
     */
    private final ObjectMapper objectMapper;

    /**
     * The HttpClient instance used to send HTTP requests.
     */
    private final HttpClient client;
    /**
     * The base URL of the API server.
     * This is used to construct the full API URL for requests.
     */
    private final String BASE_URL = "http://127.0.0.1";
    /**
     * The port on which the API server is running.
     * This is used to construct the full API URL for requests.
     */
    private final String PORT = "15400";
    /**
     * The timeout duration for HTTP requests in seconds.
     * This is used to set the connection timeout for the HttpClient.
     */
    private final long TIMEOUT_SECONDS = 10L;

    public PwssHttpClient() {
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Sends an asynchronous HTTP request to the specified endpoint using the given method, body, and headers.
     *
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @param headers  A map of headers to include in the request. Can be null if no headers are needed.
     * @return A `CompletableFuture` containing the response body as a String.
     */
    public CompletableFuture<String> request(Endpoint endpoint, String body, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(getApiUrl(endpoint)))
                .timeout(Duration.ofSeconds(10));

        // Apply method
        switch (endpoint.getMethod()) {
            case GET -> builder.GET();
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
            case PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));
            case DELETE -> {
                if (body != null && !body.isEmpty()) {
                    builder.method("DELETE", HttpRequest.BodyPublishers.ofString(body));
                } else {
                    builder.DELETE();
                }
            }
        }

        // Apply headers
        if (headers != null) {
            headers.forEach(builder::header);
        }

        // Build the request
        HttpRequest request = builder.build();

        // Send the request asynchronously and return the response body as a CompletableFuture
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Sends an asynchronous HTTP request to the specified endpoint and parses the response body into an object of the specified type.
     *
     * @param <T>      The type of the object to parse the response body into.
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @param headers  A map of headers to include in the request. Can be null if no headers are needed.
     * @param clazz    The `Class` object representing the type to parse the response body into.
     * @return A `CompletableFuture` containing the parsed object of type `T`.
     * @throws RuntimeException If the response body cannot be parsed into the specified type.
     */
    public <T> CompletableFuture<T> request(Endpoint endpoint, String body, Map<String, String> headers, Class<T> clazz) {
        return request(endpoint, body, headers)
                .thenApply(responseBody -> {
                    try {
                        return objectMapper.readValue(responseBody, clazz);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response body", e);
                    }
                });
    }

    /**
     * Constructs the full API URL for a given endpoint.
     *
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @return The full API URL as a String, combining the base URL and the endpoint's path.
     */
    private String getApiUrl(Endpoint endpoint) {
        return String.format("%s:%s%s", BASE_URL, PORT, endpoint.getUrl());
    }
}
