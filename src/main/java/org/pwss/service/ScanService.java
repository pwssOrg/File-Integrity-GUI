package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.scan.GetAllMostRecentScansException;
import org.pwss.exception.scan.GetMostRecentScansException;
import org.pwss.exception.scan.GetScanDiffsException;
import org.pwss.exception.scan.LiveFeedException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StartScanByIdException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.Scan;
import org.pwss.model.request.scan.GetMostRecentScansRequest;
import org.pwss.model.request.scan.GetScanDiffsRequest;
import org.pwss.model.request.scan.StartSingleScanRequest;
import org.pwss.model.response.LiveFeedResponse;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;

/**
 * The `ScanService` class provides methods to manage scans, specifically starting and stopping scans.
 */
public class ScanService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    public ScanService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Starts a scan by sending a request to the START_SCAN endpoint.
     *
     * @return `true` if the scan start request is successful, otherwise `false`.
     * @throws StartScanAllException If the scan start attempt fails due to various reasons such as invalid credentials, no active monitored directories, scan already running, or server error.
     * @throws ExecutionException    If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException  If the thread executing the request is interrupted.
     */
    public boolean startScan() throws StartScanAllException, ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.START_SCAN, null);

        return switch (response.statusCode()) {
            case 200 -> true;
            case 401 ->
                    throw new StartScanAllException("Start scan all failed: User not authorized to perform this action.");
            case 412 ->
                    throw new StartScanAllException("Start scan all failed: No active monitored directories found.");
            case 425 -> throw new StartScanAllException("Start scan all failed: Scan is already running.");
            case 500 ->
                    throw new StartScanAllException("Start scan all failed: An error occurred on the server while attempting to start the scan.");
            default -> false;
        };
    }

    /**
     * Starts a scan for a specific monitored directory by its ID by sending a request to the START_SCAN_ID endpoint.
     *
     * @param id The ID of the monitored directory to start the scan for.
     * @return `true` if the scan start request is successful, otherwise `false`.
     * @throws StartScanByIdException  If the scan start attempt fails due to various reasons such as invalid credentials, monitored directory not found, monitored directory inactive, scan already running, or server error.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     * @throws JsonProcessingException If an error occurs while serializing the start scan request to JSON.
     */
    public boolean startScanById(long id) throws StartScanByIdException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new StartSingleScanRequest(id));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.START_SCAN_ID, body);

        return switch (response.statusCode()) {
            case 200 -> true;
            case 401 ->
                    throw new StartScanByIdException("Start scan by id failed: User not authorized to perform this action.");
            case 404 ->
                    throw new StartScanByIdException("Start scan by id failed: Monitored directory with the given ID not found.");
            case 412 ->
                    throw new StartScanByIdException("Start scan by id failed: The monitored directory is inactive.");
            case 425 -> throw new StartScanByIdException("Start scan by id failed: Scan is already running.");
            case 500 ->
                    throw new StartScanByIdException("Start scan by id failed: An error occurred on the server while attempting to start the scan.");
            default -> false;
        };
    }

    /**
     * Stops a scan by sending a request to the STOP_SCAN endpoint.
     *
     * @return `true` if the scan stop request is successful, otherwise `false`.
     * @throws StopScanException    If the scan stop attempt fails due to various reasons such as invalid credentials or server error.
     * @throws ExecutionException   If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException If the thread executing the request is interrupted.
     */
    public boolean stopScan() throws StopScanException, ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.STOP_SCAN, null);

        return switch (response.statusCode()) {
            case 200 -> true;
            case 401 -> throw new StopScanException("Stop scan failed: User not authorized to perform this action.");
            case 500 ->
                    throw new StopScanException("Stop scan failed: An error occurred on the server while attempting to stop the scan.");
            default -> false;
        };
    }

    /**
     * Retrieves the live feed of scan events by sending a request to the LIVE_FEED endpoint.
     *
     * @return A LiveFeedResponse object containing the live feed data if the request is successful.
     * @throws LiveFeedException       If the attempt to retrieve the live feed fails due to various reasons such as invalid credentials or server error.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     * @throws JsonProcessingException If an error occurs while processing JSON data.
     */
    public LiveFeedResponse getLiveFeed() throws LiveFeedException, ExecutionException, InterruptedException, JsonProcessingException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.LIVE_FEED, null);

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), LiveFeedResponse.class);
            case 401 ->
                    throw new LiveFeedException("Failed to fetch live feed: User not authorized to perform this action.");
            case 500 ->
                    throw new LiveFeedException("Failed to fetch live feed: an error occurred on the server while attempting to fetch the live feed.");
            default -> throw new LiveFeedException("Failed to fetch live feed: unexpected response.");
        };
    }

    /**
     * Checks if a scan is currently running by sending a request to the SCAN_STATUS endpoint.
     *
     * @return `true` if a scan is running, otherwise `false`.
     * @throws ScanStatusException  If the scan status check fails due to various reasons such as invalid credentials or server error.
     * @throws ExecutionException   If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException If the thread executing the request is interrupted.
     */
    public boolean scanRunning() throws ScanStatusException, ExecutionException, InterruptedException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SCAN_STATUS, null);

        return switch (response.statusCode()) {
            case 200 -> Boolean.parseBoolean(response.body());
            case 401 ->
                    throw new ScanStatusException("Scan status check failed: User not authorized to perform this action.");
            case 500 ->
                    throw new ScanStatusException("Scan status check failed: An error occurred on the server while attempting to check the scan status.");
            default -> false;
        };
    }

    /**
     * Retrieves the most recent scans for a specified number of active monitored directories by sending a request to the MOST_RECENT_SCANS endpoint.
     *
     * @param nrOfScans The number of most recent scans to retrieve.
     * @return A list of the most recent Scan objects if the request is successful.
     * @throws GetMostRecentScansException If the attempt to retrieve the most recent scans fails due to various reasons such as invalid credentials, no active monitored directories, or server error.
     * @throws ExecutionException          If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException        If the thread executing the request is interrupted.
     * @throws JsonProcessingException     If an error occurs while processing JSON data.
     */
    public List<Scan> getMostRecentScans(long nrOfScans) throws GetMostRecentScansException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new GetMostRecentScansRequest(nrOfScans));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MOST_RECENT_SCANS, body);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), Scan[].class));
            case 401 ->
                    throw new GetMostRecentScansException("Failed to fetch most recent scans: User not authorized to perform this action.");
            case 500 -> throw new GetMostRecentScansException("Failed to fetch most recent scans: Server error");
            default -> Collections.emptyList();
        };
    }

    /**
     * Retrieves the most recent scans for all active monitored directories by sending a request to the MOST_RECENT_SCANS_ALL endpoint.
     *
     * @return A list of the most recent Scan objects if the request is successful.
     * @throws GetAllMostRecentScansException If the attempt to retrieve the most recent scans fails due to various reasons such as invalid credentials, no active monitored directories, or server error.
     * @throws ExecutionException             If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException           If the thread executing the request is interrupted.
     * @throws JsonProcessingException        If an error occurs while processing JSON data.
     */
    public List<Scan> getMostRecentScansAll() throws GetAllMostRecentScansException, ExecutionException, InterruptedException, JsonProcessingException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MOST_RECENT_SCANS_ALL, null);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), Scan[].class));
            case 401 ->
                    throw new GetAllMostRecentScansException("Failed to fetch most recent scans: User not authorized to perform this action.");
            case 500 -> throw new GetAllMostRecentScansException("Failed to fetch most recent scans: Server error");
            default -> Collections.emptyList();
        };
    }

    public List<Diff> getDiffs(long scanId, long limit, String sortField, boolean ascending) throws GetScanDiffsException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new GetScanDiffsRequest(scanId, limit, sortField, ascending));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SCAN_DIFFS, body);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), Diff[].class));
            case 401 ->
                    throw new GetScanDiffsException("Failed to fetch scan diffs: User not authorized to perform this action.");
            case 500 -> throw new GetScanDiffsException("Failed to fetch scan diffs: Server error");
            default -> Collections.emptyList();
        };
    }
}
