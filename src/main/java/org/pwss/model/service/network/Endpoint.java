package org.pwss.model.service.network;

/**
 * Enum representing various API endpoints.
 * Each enum constant is associated with a specific URL path.
 */
public enum Endpoint {

    // User Endpoints
    /**
     * Endpoint for checking if a user exists.
     */
    USER_EXISTS(HTTP_Method.GET, A.BASE_URL + A.USER + "exists"),
    /**
     * Endpoint for creating a user.
     */
    CREATE_USER(HTTP_Method.POST, A.BASE_URL + A.USER + "create"),
    /**
     * Endpoint for user login.
     */
    LOGIN(HTTP_Method.POST, A.BASE_URL + A.USER + "login"),


    // Scan Endpoints
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
     * Endpoint most recent scans for given count of active monitored directories.
     */
    MOST_RECENT_SCANS(HTTP_Method.POST, A.BASE_URL + A.SCAN + "scan/most-recent"),
    /**
     * Endpoint most recent scans for all active monitored directories.
     */
    MOST_RECENT_SCANS_ALL(HTTP_Method.GET, A.BASE_URL + A.SCAN + "active-directory/most-recent"),
    /**
     * Endpoint for retrieving all scans.
     */
    SCAN_DIFFS(HTTP_Method.POST, A.BASE_URL + A.SCAN + "diffs"),


    // Directory Endpoints
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
    MONITORED_DIRECTORY_UPDATE(HTTP_Method.PUT, A.BASE_URL + A.DIRECTORY + "update"),

    // Scan History Endpoints
    /**
     * Endpoint for retrieving the scan summaries for a specific scan.
     */
    SUMMARY_SCAN(HTTP_Method.POST, A.BASE_URL + A.SCAN_SUMMARY + "scan"),
    /**
     * Endpoint for retrieving the scan summaries for a specific file.
     */
    SUMMARY_FILE(HTTP_Method.POST, A.BASE_URL + A.SCAN_SUMMARY + "file"),
    /**
     * Endpoint for retrieving the most recent scan summary.
     */
    SUMMARY_MOST_RECENT_SCAN(HTTP_Method.POST, A.BASE_URL + A.SCAN_SUMMARY + "most-recent");

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
     * Path segment for scan history-related endpoints.
     */
    static final String SCAN_SUMMARY = "summary/";
    /**
     * Path segment for user-related endpoints.
     */
    static final String USER = "user/";
}