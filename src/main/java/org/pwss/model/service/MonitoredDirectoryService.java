package org.pwss.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.monitored_directory.NewMonitoredDirectoryException;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.service.network.Endpoint;
import org.pwss.model.service.network.PwssHttpClient;
import org.pwss.model.service.request.monitored_directory.NewDirectoryRequest;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MonitoredDirectoryService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    public MonitoredDirectoryService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves all monitored directories by sending a request to the MONITORED_DIRECTORY_ALL endpoint.
     *
     * @return A list of MonitoredDirectory objects representing all monitored directories.
     * @throws MonitoredDirectoryGetAllException If the retrieval attempt fails due to various reasons such as invalid credentials or server error.
     * @throws ExecutionException                If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException              If the thread executing the request is interrupted.
     * @throws JsonProcessingException           If an error occurs while deserializing the response body to MonitoredDirectory objects.
     */
    public List<MonitoredDirectory> getAllDirectories() throws MonitoredDirectoryGetAllException, ExecutionException, InterruptedException, JsonProcessingException {
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MONITORED_DIRECTORY_ALL, null);

        return switch (response.statusCode()) {
            case 200 -> List.of(objectMapper.readValue(response.body(), MonitoredDirectory[].class));
            case 401 ->
                    throw new MonitoredDirectoryGetAllException("Get all monitored directories failed: User not authorized to perform this action.");
            case 500 ->
                    throw new MonitoredDirectoryGetAllException("Get all monitored directories failed: An error occurred on the server while attempting to retrieve the monitored directories.");
            default -> List.of();
        };
    }

    /**
     * Creates a new monitored directory by sending a request to the MONITORED_DIRECTORY_CREATE endpoint.
     *
     * @param path                 The path of the directory to be monitored.
     * @param includeSubdirectories A boolean indicating whether subdirectories should be included.
     * @param isActive             A boolean indicating whether the monitored directory should be active.
     * @return A `MonitoredDirectory` object representing the newly created monitored directory.
     * @throws NewMonitoredDirectoryException If the creation attempt fails due to invalid input, unauthorized access, or server error.
     * @throws ExecutionException             If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException           If the thread executing the request is interrupted.
     * @throws JsonProcessingException        If an error occurs while serializing the request body or deserializing the response body.
     */
    public MonitoredDirectory createNewMonitoredDirectory(String path, boolean includeSubdirectories, boolean isActive) throws NewMonitoredDirectoryException, ExecutionException, InterruptedException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(new NewDirectoryRequest(path, includeSubdirectories, isActive));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MONITORED_DIRECTORY_CREATE, body);

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), MonitoredDirectory.class);
            case 400 ->
                    throw new NewMonitoredDirectoryException("Create monitored directory failed: Invalid directory path or request body.");
            case 401 ->
                    throw new NewMonitoredDirectoryException("Create monitored directory failed: User not authorized to perform this action.");
            case 500 ->
                    throw new NewMonitoredDirectoryException("Create monitored directory failed: An error occurred on the server while attempting to create the monitored directory.");
            default -> null;
        };
    }
}
