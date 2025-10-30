package org.pwss.service.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.pwss.exception.ssl.SSLsetupErrorException;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton HTTP client for making requests to the API server.
 * This class provides methods for sending asynchronous and synchronous HTTP requests,
 * handling JSON serialization/deserialization, and managing session cookies.
 *
 * @author PWSS ORG
 */
public class PwssHttpClient {

    /**
     * Logger instance for logging messages within this class.
     */
    private static Logger log = LoggerFactory.getLogger(PwssHttpClient.class);

    /**
     * A singleton instance of the `PwssHttpClient` class.
     * Ensures that only one instance of the client is created and shared across the application.
     */
    private static PwssHttpClient instance;
    /**
     * The ObjectMapper instance used for JSON serialization and deserialization.
     * This is typically used to convert Java objects to JSON and vice versa.
     */
    private final ObjectMapper objectMapper;
    /**
     * The HttpClient instance used to send HTTP requests.
     */
    private  HttpClient client;
    /**
     * The base URL of the API server.
     * This is used to construct the full API URL for requests.
     */
    private final String BASE_URL = "https://localhost";
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
    /**
     * Default headers to be included in every request.
     * This can be used to set common headers like Content-Type, User-Agent, etc.
     */
    private final Map<String, String> defaultHeaders;
    /**
     * Session variables like header fields and Session Cookie(s)
     */
    private Session session;

    private PwssHttpClient() {
        this.objectMapper = new ObjectMapper();
        try {
            this.client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .sslContext(SSLSetup.createSSLcontext())
                    .build();
        } catch (SSLsetupErrorException e) {
           log.error("Fatal SSL ERROR",e.getMessage());
        }
        this.defaultHeaders = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
    }

    public static synchronized PwssHttpClient getInstance() {
        if (instance == null) {
            instance = new PwssHttpClient();
        }
        return instance;
    }

    /**
     * Clears the current session, removing any stored session cookies or headers.
     */
    public void clearSession() {
        this.session = null;
    }

    /**
     * Sends an asynchronous HTTP request to the specified endpoint using the given method, body, and headers.
     *
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @return A `CompletableFuture` containing the response body as a String.
     */
    public CompletableFuture<HttpResponse<String>> requestAsync(Endpoint endpoint, String body) {
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
            default -> throw new IllegalArgumentException("Unexpected value: " + endpoint.getMethod());
        }

        // Apply default headers
        defaultHeaders.forEach(builder::header);

        // Apply session cookie if available
        if (session != null && session.getSessionCookie().isPresent()) {
            builder.header("Cookie", session.getSessionCookie().get());
        }

        // Build the request
        HttpRequest request = builder.build();

        // Send the request asynchronously and return the response body as a CompletableFuture
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    // Session management: update session if headers contain Set-Cookie
                    Session.from(response.headers()).ifPresent(newSession -> {
                        this.session = newSession;

                    });
                    // Return the full response
                    return response;
                });
    }

    /**
     * Sends a synchronous HTTP request to the specified endpoint using the given method and body.
     * This method blocks the current thread until the request is completed.
     *
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @return The `HttpResponse` object containing the response from the server.
     * @throws InterruptedException If the current thread is interrupted while waiting for the response.
     * @throws ExecutionException   If an exception occurs while processing the request.
     */
    public HttpResponse<String> request(Endpoint endpoint, String body) throws InterruptedException, ExecutionException {
        return requestAsync(endpoint, body).get();
    }

    /**
     * Sends an asynchronous HTTP request to the specified endpoint and parses the response body into an object of the specified type.
     *
     * @param <T>      The type of the object to parse the response body into.
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @param clazz    The `Class` object representing the type to parse the response body into.
     * @return A `CompletableFuture` containing the parsed object of type `T`.
     */
    public <T> CompletableFuture<T> requestAsync(Endpoint endpoint, String body, Class<T> clazz) {
        return requestAsync(endpoint, body)
                .thenCompose(response -> {
                    try {
                        T parsed = objectMapper.readValue(response.body(), clazz);
                        return CompletableFuture.completedFuture(parsed);
                    } catch (IOException e) {
                        // JSON parsing / mapping issue
                        return CompletableFuture.failedFuture(e);
                    } catch (Exception e) {
                        if (response.statusCode() == 401) {
                            // If we get a 401 Unauthorized, log the user out
                            NavigationEvents.navigateTo(Screen.LOGIN);
                            this.clearSession();
                        }
                        // Any other unexpected error
                        return CompletableFuture.failedFuture(e);
                    }
                });
    }

    /**
     * Sends a synchronous HTTP request to the specified endpoint and parses the response body into an object of the specified type.
     * This method blocks the current thread until the request is completed.
     *
     * @param <T>      The type of the object to parse the response body into.
     * @param endpoint The `Endpoint` enum constant representing the API endpoint.
     * @param body     The request body as a String. Can be null or empty for methods like GET or DELETE.
     * @param clazz    The `Class` object representing the type to parse the response body into.
     * @return The parsed object of type `T`.
     * @throws InterruptedException If the current thread is interrupted while waiting for the response.
     * @throws ExecutionException   If an exception occurs while processing the request or parsing the response.
     */
    public <T> T request(Endpoint endpoint, String body, Class<T> clazz) throws InterruptedException, ExecutionException {
        return requestAsync(endpoint, body, clazz).get();
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
