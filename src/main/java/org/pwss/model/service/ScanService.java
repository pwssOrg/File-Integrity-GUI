package org.pwss.model.service;

import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.util.HttpUtility;
import org.pwss.model.service.network.PwssHttpClient;
import java.util.concurrent.CompletableFuture;

/**
 * The `ScanService` class provides methods to manage scans, specifically starting and stopping scans.
 */
public class ScanService {

    public ScanService() {
    }

    /**
     * Asynchronously starts a scan by sending a request to the START_SCAN endpoint.
     *
     * @return A `CompletableFuture` that resolves to `true` if the scan request is successful, otherwise `false`.
     * @throws RuntimeException If an exception occurs during the request.
     */
    public CompletableFuture<Boolean> startScan() {
        try {
            return PwssHttpClient.getInstance().requestAsync(Endpoint.START_SCAN, null)
                    .thenApply(response -> HttpUtility.responseIsSuccess(response.statusCode()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asynchronously stops a scan by sending a request to the STOP_SCAN endpoint.
     *
     * @return A `CompletableFuture` that resolves to `true` if the scan stop request is successful, otherwise `false`.
     * @throws RuntimeException If an exception occurs during the request.
     */
    public CompletableFuture<Boolean> stopScan() {
        try {
            return PwssHttpClient.getInstance().requestAsync(Endpoint.STOP_SCAN, null)
                    .thenApply(response -> HttpUtility.responseIsSuccess(response.statusCode()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
