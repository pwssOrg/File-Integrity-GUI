package org.pwss.model.service.network;


import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an HTTP session with headers and a session cookie (JSESSIONID).
 */
public final class Session {

    private final Map<String, List<String>> headerFields;
    private final List<String> cookies;
    private final String sessionCookie;

    private Session(Map<String, List<String>> headerFields, List<String> cookies, String sessionCookie) {
        this.headerFields = Map.copyOf(headerFields);
        this.cookies = List.copyOf(cookies);
        this.sessionCookie = sessionCookie;
    }

    /**
     * Factory method that builds a Session from HttpHeaders.
     */
    public static Optional<Session> from(HttpHeaders headers) {
        return headers.firstValue("Set-Cookie")
                .map(cookieHeader -> {
                    String sessionCookie = parseSessionCookie(cookieHeader);
                    return new Session(headers.map(), List.of(cookieHeader), sessionCookie);
                });
    }

    private static String parseSessionCookie(String cookieHeader) {
        return java.util.Arrays.stream(cookieHeader.split(";"))
                .map(String::trim)
                .filter(s -> s.startsWith("JSESSIONID="))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JSESSIONID not found in Set-Cookie"));
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public Optional<String> getSessionCookie() {
        return Optional.ofNullable(sessionCookie);
    }
}
