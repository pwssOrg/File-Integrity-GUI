package org.pwss.model.service.network;

/**
 * Enum representing various API endpoints.
 * Each enum constant is associated with a specific URL path.
 */
public enum Endpoint {
    /**
     * Endpoint for creating a user.
     */
    CREATE_USER(Method.POST, "/api/users/create-user"),
    /**
     * Endpoint for user login.
     */
    LOGIN(Method.POST, "/api/users/login"),
    /**
     * Endpoint for starting a file integrity scan.
     */
    START_SCAN(Method.POST, "/api/file-integrity/start-scan"),
    /**
     * Endpoint for stopping a file integrity scan.
     */
    STOP_SCAN(Method.POST, "/api/file-integrity/stop-scan");

    /**
     * The URL path associated with the endpoint.
     */
    private final String url;
    /**
     * The HTTP method associated with the endpoint.
     */
    private final Method method;

    /**
     * Constructor for the Endpoint enum.
     *
     * @param url The URL path associated with the endpoint.
     */
    Endpoint(Method method, String url) {
        this.method = method;
        this.url = url;
    }

    /**
     * Retrieves the HTTP method associated with the endpoint.
     *
     * @return The HTTP method as a Method enum constant.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Retrieves the URL path associated with the endpoint.
     *
     * @return The URL path as a String.
     */
    public String getUrl() {
        return url;
    }
}
