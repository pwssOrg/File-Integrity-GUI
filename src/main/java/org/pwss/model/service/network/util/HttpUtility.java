package org.pwss.model.service.network.util;

public final class HttpUtility {
    /**
     * Determines if an HTTP response code indicates a successful response.
     *
     * @param responseCode The HTTP response code to evaluate.
     * @return `true` if the response code is in the range 200-299 (inclusive), otherwise `false`.
     */
    public static boolean responseIsSuccess(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }
}
