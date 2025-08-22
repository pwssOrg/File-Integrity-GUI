package org.pwss.model.service;

import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.util.HttpUtility;
import org.pwss.model.service.network.PwssHttpClient;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * The `ScanService` class provides methods to manage scans, specifically starting and stopping scans.
 */
public class ScanService {

    public ScanService() {
    }

    /**
     * Starts a scan by sending a request to the START_SCAN endpoint.
     *
     * @return `true` if the scan start request is successful (HTTP status indicates success), otherwise `false`.
     */
    public boolean startScan() throws ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.START_SCAN, null);
        return HttpUtility.responseIsSuccess(response.statusCode());
    }

    /**
     * Stops a scan by sending a request to the STOP_SCAN endpoint.
     *
     * @return `true` if the scan stop request is successful (HTTP status indicates success), otherwise `false`.
     */
    public boolean stopScan() throws ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.STOP_SCAN, null);
        return HttpUtility.responseIsSuccess(response.statusCode());
    }
}
