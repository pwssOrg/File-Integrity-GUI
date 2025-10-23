package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.file.QuarantineFileException;
import org.pwss.exception.file.UnquarantineFileException;
import org.pwss.exception.metadata.MetadataRemoveException;
import org.pwss.exception.metadata.MetadataSaveException;
import org.pwss.metadata.MetadataManager;
import org.pwss.model.request.file.QuarantineRequest;
import org.pwss.model.request.file.UnquarantineRequest;
import org.pwss.model.response.QuarantineResponse;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;
import org.slf4j.Logger;

/**
 * The `FileService` class provides methods for quarantining and unquarantining files.
 */
public class FileService {
    /**
     * Logger instance for logging information and errors.
     */
    private final Logger log;
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;
    /**
     * An instance of MetadataManager for managing metadata operations.
     */
    private final MetadataManager metadataManager;

    /**
     * Constructs a new `FileService` instance.
     */
    public FileService() {
        this.log = org.slf4j.LoggerFactory.getLogger(FileService.class);
        this.objectMapper = new ObjectMapper();
        this.metadataManager = new MetadataManager();
    }

    /**
     * Quarantines a file by sending a request to the quarantine endpoint.
     *
     * @param fileId The ID of the file to be quarantined.
     * @return true if the file was successfully quarantined; false otherwise.
     * @throws QuarantineFileException  If the file cannot be quarantined due to various reasons such as
     *                                  unauthorized access, unprocessable entity, or server errors.
     * @throws JsonProcessingException  If there is an error during JSON serialization or deserialization.
     * @throws ExecutionException       If an exception occurs during the asynchronous operation.
     * @throws InterruptedException     If the operation is interrupted while waiting for the response.
     * @throws MetadataSaveException    If there is an error saving metadata for the quarantined file.
     */
    public boolean quarantineFile(long fileId) throws QuarantineFileException, JsonProcessingException, ExecutionException, InterruptedException, MetadataSaveException {
        final String body = objectMapper.writeValueAsString(new QuarantineRequest(fileId));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.QUARANTINE_FILE, body);

        Optional<QuarantineResponse> parsed = (response.statusCode() == 200)
                ? Optional.ofNullable(objectMapper.readValue(response.body(), QuarantineResponse.class))
                : Optional.empty();

        if (parsed.isPresent()) {
            QuarantineResponse res = parsed.get();
            handleQuarantine(res.keyName(), fileId);
        }

        return switch (response.statusCode()) {
            case 200 -> parsed.map(QuarantineResponse::successful).orElse(false);
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

    /**
     * Handles the metadata saving process for a quarantined file.
     *
     * @param keyName The unique key name of the quarantined file.
     * @param fileId  The ID of the file that was quarantined.
     * @throws MetadataSaveException If there is an error saving metadata for the quarantined file.
     */
    private void handleQuarantine(String keyName, Long fileId) throws MetadataSaveException {
        log.debug("Saving metadata for quarantined file. KeyName: {}, FileID: {}", keyName, fileId);
        metadataManager.saveMetadataForQuarantinedFile(keyName, fileId);
    }

    /**
     * Handles the metadata removal process for an unquarantined file.
     *
     * @param fileId The ID of the file that was unquarantined.
     * @throws MetadataRemoveException If there is an error removing metadata for the unquarantined file.
     */
    private void handleUnquarantine(Long fileId) throws MetadataRemoveException {
        log.debug("Removing metadata for unquarantined file. FileID: {}", fileId);
        metadataManager.removeMetadataForUnQuarantinedFile(fileId);
    }
}
