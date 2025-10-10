package org.pwss.service.network;

/**
 * Enum representing HTTP methods used in RESTful web services.
 * <p>
 * For more information, see {@link <a href=
 * "https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Methods">developer.mozilla.org</a>}.
 */
public enum HTTP_Method {
    /**
     * The HTTP GET method is used to retrieve information from the server.
     */
    GET,
    /**
     * The HTTP POST method is used to send data to the server.
     */
    POST,
    /**
     * The HTTP PUT method is used to update a resource on the server.
     */
    PUT,
    /**
     * The HTTP DELETE method is used to remove a resource from the server.
     */
    DELETE,
    /**
     * The HTTP HEAD method is similar to GET, but it retrieves only the headers
     * without the message body.
     */
    HEAD,
    /**
     * The HTTP CONNECT method is used to establish a tunnel to the server.
     */
    CONNECT,
    /**
     * The HTTP TRACE method performs a message loop-back test along the path
     * to the target resource.
     */
    TRACE,
    /**
     * The HTTP PATCH method is used for partial modifications of a resource.
     */
    PATCH,
    /**
     * The HTTP OPTIONS method describes the communication options for the target
     * resource.
     */
    OPTIONS
}
