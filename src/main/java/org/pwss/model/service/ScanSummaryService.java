package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.scan_summary.GetMostRecentSummaryException;
import org.pwss.exception.scan_summary.GetSearchFilesException;
import org.pwss.exception.scan_summary.GetSummaryForFileException;
import org.pwss.exception.scan_summary.GetSummaryForScanException;
import org.pwss.model.entity.File;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.PwssHttpClient;
import org.pwss.model.service.request.scan_summary.GetFilesSearchRequest;
import org.pwss.model.service.request.scan_summary.GetSummaryForFileRequest;
import org.pwss.model.service.request.scan_summary.GetSummaryForScanRequest;

public class ScanSummaryService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    public ScanSummaryService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves the most recent scan summary by sending a request to the SUMMARY_MOST_RECENT_SCAN endpoint.
     *
     * @return The most recent ScanSummary object if the request is successful.
     * @throws GetMostRecentSummaryException If the attempt to retrieve the most recent scan summary fails due to various reasons such as invalid credentials, no scan summaries found, or server error.
     * @throws ExecutionException            If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException          If the thread executing the request is interrupted.
     * @throws JsonProcessingException       If an error occurs while processing JSON data.
     */
    public List<ScanSummary> getMostRecentSummary() throws GetMostRecentSummaryException, ExecutionException, InterruptedException, JsonProcessingException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SUMMARY_MOST_RECENT_SCAN, null);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), ScanSummary[].class));
            case 401 ->
                    throw new GetMostRecentSummaryException("Get most recent scan summary failed: User not authorized to perform this action.");
            case 404 ->
                    throw new GetMostRecentSummaryException("Get most recent scan summary failed: No scan summaries found.");
            case 500 ->
                    throw new GetMostRecentSummaryException("Get most recent scan summary failed: An error occurred on the server while attempting to retrieve the scan summary.");
            default -> null;
        };
    }

    /**
     * Retrieves scan summaries for a specific file by sending a request to the SUMMARY_FILE endpoint.
     *
     * @param fileId The ID of the file for which to retrieve scan summaries.
     * @return A list of ScanSummary objects associated with the specified file ID if the request is successful.
     * @throws GetSummaryForFileException If the attempt to retrieve scan summaries for the specified file fails due to various reasons such as invalid credentials, no scan summaries found, invalid file ID, or server error.
     * @throws ExecutionException         If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException       If the thread executing the request is interrupted.
     * @throws JsonProcessingException    If an error occurs while processing JSON data.
     */
    public List<ScanSummary> getSummaryForFile(long fileId) throws GetSummaryForFileException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new GetSummaryForFileRequest(fileId));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SUMMARY_FILE, body);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), ScanSummary[].class));
            case 401 ->
                    throw new GetSummaryForFileException("Get summaries for file failed: User not authorized to perform this action.");
            case 404 ->
                    throw new GetSummaryForFileException("Get summaries for file failed: No scan summaries found for the specified file.");
            case 422 ->
                    throw new GetSummaryForFileException("Get summaries for file failed: The provided file ID is invalid.");
            case 500 ->
                    throw new GetSummaryForFileException("Get summaries for file failed: An error occurred on the server while attempting to retrieve the scan summaries.");
            default -> null;
        };
    }

    /**
     * Searches for files based on a query string by sending a request to the SUMMARY_FILE_SEARCH endpoint.
     *
     * @param queryString The search query string used to find files.
     * @param ascending   A boolean indicating whether the search results should be sorted in ascending order.
     * @return A list of File objects that match the search criteria if the request is successful.
     * @throws GetSearchFilesException If the attempt to search for files fails due to various reasons such as invalid credentials, invalid search parameters, or server error.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     * @throws JsonProcessingException If an error occurs while processing JSON data.
     */
    public List<File> searchFiles(String queryString, boolean ascending) throws GetSearchFilesException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new GetFilesSearchRequest(queryString, 1000, "basename", ascending));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SUMMARY_FILE_SEARCH, body);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), File[].class));
            case 401 ->
                    throw new GetSearchFilesException("Search files failed: User not authorized to perform this action.");
            case 404 -> List.of();
            case 422 ->
                    throw new GetSearchFilesException("Search files failed: The provided search parameters are invalid.");
            case 500 ->
                    throw new GetSearchFilesException("Search files failed: An error occurred on the server while attempting to search for files.");
            default -> null;
        };
    }

    public List<ScanSummary> getScanSummaryForScan(long scanId) throws GetSummaryForScanException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new GetSummaryForScanRequest(scanId));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.SUMMARY_SCAN, body);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), ScanSummary[].class));
            case 401 ->
                    throw new GetSummaryForScanException("Get summaries for scan failed: User not authorized to perform this action.");
            case 404 ->
                    throw new GetSummaryForScanException("Get summaries for scan failed: No scan summaries found for the specified file.");
            case 422 ->
                    throw new GetSummaryForScanException("Get summaries for scan failed: The provided scan ID is invalid.");
            case 500 ->
                    throw new GetSummaryForScanException("Get summaries for scan failed: An error occurred on the server while attempting to retrieve the scan summaries.");
            default -> null;
        };
    }
}
