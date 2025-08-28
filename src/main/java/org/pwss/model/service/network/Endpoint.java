package org.pwss.model.service.network;

/**
 * Enum representing various API endpoints.
 * Each enum constant is associated with a specific URL path.
 */
public enum Endpoint {

    /**
     * Endpoint for checking if a user exists.
     */
    USER_EXISTS(HTTP_Method.GET, A.BASE_URL + A.USER + "exists"),
    /**
     * Endpoint for creating a user.
     */
    CREATE_USER(HTTP_Method.POST, A.BASE_URL + A.USER + "create-user"),
    /**
     * Endpoint for user login.
     */
    LOGIN(HTTP_Method.POST, A.BASE_URL + A.USER + "login"),
    /**
     * Endpoint for starting a file integrity scan of all directories.
     */
    START_SCAN(HTTP_Method.GET, A.BASE_URL + A.SCAN + "start/all"),

    /**
     * Endpoint for starting a file integrity scan of a specific directory.
     */
    START_SCAN_ID(HTTP_Method.POST, A.BASE_URL + A.SCAN + "start/id"),
    /**
     * Endpoint for stopping a file integrity scan.
     */
    STOP_SCAN(HTTP_Method.GET, A.BASE_URL + A.SCAN + "stop"),
    /**
     * Endpoint for retrieving the status of a file integrity scan.
     */
    SCAN_STATUS(HTTP_Method.GET, A.BASE_URL + A.SCAN + "status"),
    /**
     * Endpoint for retrieving all monitored directories.
     */
    MONITORED_DIRECTORY_ALL(HTTP_Method.GET, A.BASE_URL + A.DIRECTORY + "all"),
    /**
     * Endpoint for retrieving a monitored directory by its ID.
     */
    MONITORED_DIRECTORY_BY_ID(HTTP_Method.POST, A.BASE_URL + A.DIRECTORY + "id"),
    /**
     * Endpoint for creating a new monitored directory.
     */
    MONITORED_DIRECTORY_CREATE(HTTP_Method.POST, A.BASE_URL + A.DIRECTORY + "new"),
    /**
     * Endpoint for creating a new baseline for a monitored directory.
     */
    MONITORED_DIRECTORY_NEW_BASELINE(HTTP_Method.POST, A.BASE_URL + A.DIRECTORY + "new-baseline"),
    /**
     * Endpoint for updating an existing monitored directory.
     */
    MONITORED_DIRECTORY_UPDATE(HTTP_Method.PUT, A.BASE_URL + A.DIRECTORY + "update");

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
 * A nested final class holding endpoint constant values.
 */
final class A {

    /**
     * Base URL for all API endpoints.
     */
    static final String BASE_URL = "/api/";
    /**
     * Path segment for directory-related endpoints.
     */
    static final String DIRECTORY = "directory/";
    /**
     * Path segment for scan-related endpoints.
     */
    static final String SCAN = "scan/";

    /**
     * Path segment for user-related endpoints.
     */
    static final String USER = "user/";
}

/**
 * This Class was super nice Stefan :D
 */