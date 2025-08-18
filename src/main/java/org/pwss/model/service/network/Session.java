package org.pwss.model.service.network;


import java.util.List;
import java.util.Map;

class Session {

    private final Map<String, List<String>> headerFields;
    private final List<String> cookiesHeader;
    private String sessionCookie = null;

    Session(Map<String, List<String>> headerFields, List<String> cookiesHeader, String sessionCookie) {
        this.headerFields = headerFields;
        this.cookiesHeader = cookiesHeader;
        this.sessionCookie = sessionCookie;
    }



    Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    List<String> getCookiesHeader() {
        return cookiesHeader;
    }

    String getSessionCookie() {
        return sessionCookie;
    }

    void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

}
