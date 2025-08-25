package org.pwss.model.service.network;

/**
 * Enum representing various API endpoints.
 * Each enum constant is associated with a specific URL path.
 */
public enum Endpoint {
    /**
     * Endpoint for checking if a user exists.
     */
    USER_EXISTS(HTTP_Method.GET, "/api/user/exists"),
    /**
     * Endpoint for creating a user.
     */
    CREATE_USER(HTTP_Method.POST, "/api/user/create-user"),
    /**
     * Endpoint for user login.
     */
    LOGIN(HTTP_Method.POST, "/api/user/login"),
    /**
     * Endpoint for starting a file integrity scan.
     */
    START_SCAN(HTTP_Method.GET, "/api/scan/start/all"),

    START_SCAN_ID(HTTP_Method.POST, "/api/scan/start/id"),
    /**
     * Endpoint for stopping a file integrity scan.
     */
    STOP_SCAN(HTTP_Method.GET, "/api/scan/stop-scan"),
    /**
     * Endpoint for retrieving the status of a file integrity scan.
     */
    SCAN_STATUS(HTTP_Method.GET, "/api/scan/status"),
    /**
     * Endpoint for retrieving all monitored directories.
     */
    MONITORED_DIRECTORY_ALL(HTTP_Method.GET, "/api/directory/all"),
    /**
     * Endpoint for retrieving a monitored directory by its ID.
     */
    MONITORED_DIRECTORY_BY_ID(HTTP_Method.POST, "/api/directory/id"),
    /**
     * Endpoint for creating a new monitored directory.
     */
    MONITORED_DIRECTORY_CREATE(HTTP_Method.POST, "/api/directory/new"),
    /**
     * Endpoint for creating a new baseline for a monitored directory.
     */
    MONITORED_DIRECTORY_NEW_BASELINE(HTTP_Method.POST, "/api/directory/new-baseline"),
    /**
     * Endpoint for updating an existing monitored directory.
     */
    MONITORED_DIRECTORY_UPDATE(HTTP_Method.PUT, "/api/directory/update");

    /**
     * The URL path associated with the endpoint.
     */
    private final String url;
    /**
     * The HTTP method associated with the endpoint.
     */
    private final HTTP_Method method;

    /**
     * Constructor for the Endpoint enum.
     *
     * @param url The URL path associated with the endpoint.
     */
    Endpoint(HTTP_Method method, String url) {
        this.method = method;
        this.url = url;
    }

    /**
     * Retrieves the HTTP method associated with the endpoint.
     *
     * @return The HTTP method as a Method enum constant.
     */
    public HTTP_Method getMethod() {
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

/**
 * This Class was super nice Stefan :D 
 */