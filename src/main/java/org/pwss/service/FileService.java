package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.file.QuarantineFileException;
import org.pwss.exception.file.UnquarantineFileException;
import org.pwss.model.request.file.QuarantineRequest;
import org.pwss.model.request.file.UnquarantineRequest;
import org.pwss.model.response.QuarantineResponse;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;

/**
 * The `FileService` class provides methods for quarantining and unquarantining files.
 */
public class FileService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new `FileService` instance.
     */
    public FileService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Quarantines a file by sending a request to the quarantine endpoint.
     *
     * @param fileId The unique identifier of the file to be quarantined.
     * @return A `QuarantineResponse` object containing the response details.
     * @throws QuarantineFileException If the file cannot be quarantined due to various reasons such as
     *                                 unauthorized access, unprocessable entity, or server errors.
     * @throws JsonProcessingException If there is an error during JSON serialization or deserialization.
     * @throws ExecutionException      If an exception occurs during the asynchronous operation.
     * @throws InterruptedException    If the operation is interrupted while waiting for the response.
     */
    public QuarantineResponse quarantineFile(long fileId) throws QuarantineFileException, JsonProcessingException, ExecutionException, InterruptedException {
        final String body = objectMapper.writeValueAsString(new QuarantineRequest(fileId));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.QUARANTINE_FILE, body);

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), QuarantineResponse.class);
            case 401 -> throw new QuarantineFileException("Unauthorized: Invalid credentials for file quarantine");
            case 422 -> throw new QuarantineFileException("File cannot be quarantined: " + fileId);
            case 500 -> throw new QuarantineFileException("Server error during file quarantine");
            default -> throw new QuarantineFileException("File quarantine failed: Unexpected status code ");
        };
    }

    /**
     * Unquarantines a file by sending a request to the unquarantine endpoint.
     *
     * @param keyPath The unique key path of the file to be unquarantined.
     * @return A `QuarantineResponse` object containing the response details.
     * @throws UnquarantineFileException If the file cannot be unquarantined due to various reasons such as
     *                                   unauthorized access, unprocessable entity, or server errors.
     * @throws JsonProcessingException   If there is an error during JSON serialization or deserialization.
     * @throws ExecutionException        If an exception occurs during the asynchronous operation.
     * @throws InterruptedException      If the operation is interrupted while waiting for the response.
     */
    public QuarantineResponse unquarantineFile(String keyPath) throws UnquarantineFileException, JsonProcessingException, ExecutionException, InterruptedException {
        final String body = objectMapper.writeValueAsString(new UnquarantineRequest(keyPath));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.UNQUARANTINE_FILE, body);

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), QuarantineResponse.class);
            case 401 -> throw new UnquarantineFileException("Unauthorized: Invalid credentials for file unquarantine");
            case 422 -> throw new UnquarantineFileException("File cannot be unquarantined: " + keyPath);
            case 500 -> throw new UnquarantineFileException("Server error during file unquarantine");
            default -> throw new UnquarantineFileException("File unquarantine failed: Unexpected status code ");
        };
    }
}
