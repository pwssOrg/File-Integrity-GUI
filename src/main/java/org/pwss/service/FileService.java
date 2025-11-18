package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.file.QuarantineFileException;
import org.pwss.exception.file.UnquarantineFileException;
import org.pwss.exception.metadata.MetadataKeyNameRetrievalException;
import org.pwss.exception.metadata.MetadataRemoveException;
import org.pwss.exception.metadata.MetadataSaveException;
import org.pwss.metadata.MetadataManager;
import org.pwss.model.entity.QuarantineMetadata;
import org.pwss.model.request.file.QuarantineRequest;
import org.pwss.model.request.file.UnquarantineRequest;
import org.pwss.model.response.QuarantineResponse;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `FileService` class provides methods for quarantining and unquarantining
 * files.
 */
public class FileService {
    /**
     * Logger instance for logging information and errors.
     */
    private final Logger log;
    /**
     * An instance of the ObjectMapper used for JSON serialization and
     * deserialization.
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
        this.log = LoggerFactory.getLogger(FileService.class);
        this.objectMapper = new ObjectMapper();
        this.metadataManager = new MetadataManager();
    }

    /**
     * Quarantines a file by sending a request to the quarantine endpoint.
     *
     * @param fileId The ID of the file to be quarantined.
     * @return true if the file was successfully quarantined; false otherwise.
     * @throws QuarantineFileException If the file cannot be quarantined due to
     *                                 various reasons such as
     *                                 unauthorized access, bad request, or
     *                                 server errors.
     * @throws JsonProcessingException If there is an error during JSON
     *                                 serialization or deserialization.
     * @throws ExecutionException      If an exception occurs during the
     *                                 asynchronous operation.
     * @throws InterruptedException    If the operation is interrupted while waiting
     *                                 for the response.
     * @throws MetadataSaveException   If there is an error saving metadata for the
     *                                 quarantined file.
     */
    public boolean quarantineFile(long fileId) throws QuarantineFileException, JsonProcessingException,
            ExecutionException, InterruptedException, MetadataSaveException {
        final String body = objectMapper.writeValueAsString(new QuarantineRequest(fileId));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.QUARANTINE_FILE, body);

        Optional<QuarantineResponse> parsed = (response.statusCode() == 200)
                ? Optional.ofNullable(objectMapper.readValue(response.body(), QuarantineResponse.class))
                : Optional.empty();

        if (parsed.isPresent() && parsed.get().successful()) {
            QuarantineResponse res = parsed.get();
            handleQuarantine(res.keyName(), fileId);
        }

        return switch (response.statusCode()) {
            case 200 -> parsed.map(QuarantineResponse::successful).orElse(false);
            case 400 -> throw new QuarantineFileException("File cannot be quarantined: " + fileId);
            case 401 -> throw new QuarantineFileException("Unauthorized: Invalid credentials for file quarantine");
            case 500 -> throw new QuarantineFileException("Server error during file quarantine");
            default -> throw new QuarantineFileException("File quarantine failed: Unexpected status code ");
        };
    }

    /**
     * Unquarantines a file by sending a request to the unquarantine endpoint.
     *
     * @param metadata The `QuarantineMetadata` object containing information about
     *                 the file to be unquarantined.
     * @return A `QuarantineResponse` object containing the response details.
     * @throws UnquarantineFileException If the file cannot be unquarantined due to
     *                                   various reasons such as
     *                                   unauthorized access, bad request,
     *                                   or server errors.
     * @throws JsonProcessingException   If there is an error during JSON
     *                                   serialization or deserialization.
     * @throws ExecutionException        If an exception occurs during the
     *                                   asynchronous operation.
     * @throws InterruptedException      If the operation is interrupted while
     *                                   waiting for the response.
     * @throws MetadataRemoveException   If there is an error removing metadata for
     *                                   the unquarantined file.
     */
    public boolean unquarantineFile(QuarantineMetadata metadata) throws UnquarantineFileException,
            JsonProcessingException, ExecutionException, InterruptedException, MetadataRemoveException {
        final String body = objectMapper.writeValueAsString(new UnquarantineRequest(metadata.keyName()));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.UNQUARANTINE_FILE, body);

        Optional<QuarantineResponse> parsed = (response.statusCode() == 200)
                ? Optional.ofNullable(objectMapper.readValue(response.body(), QuarantineResponse.class))
                : Optional.empty();

        if (parsed.isPresent() && parsed.get().successful()) {
            handleUnquarantine(metadata.fileId());
        }

        return switch (response.statusCode()) {
            case 200 -> true;
            case 400 -> throw new UnquarantineFileException("File cannot be unquarantined: " + metadata.keyName());
            case 401 -> throw new UnquarantineFileException("Unauthorized: Invalid credentials for file unquarantine");
            case 500 -> throw new UnquarantineFileException("Server error during file unquarantine");
            default -> throw new UnquarantineFileException("File unquarantine failed: Unexpected status code ");
        };
    }

    /**
     * Retrieves metadata for all quarantined files.
     *
     * @return A list of `QuarantineMetadata` objects representing all quarantined
     *         files.
     * @throws MetadataKeyNameRetrievalException If there is an error retrieving key
     *                                           names for quarantined files.
     */
    public List<QuarantineMetadata> getAllQuarantinedFiles() throws MetadataKeyNameRetrievalException {
        final List<Long> quarantinedFileIds = metadataManager.getFileIdsOfAllQuarantinedFiles();
        final List<QuarantineMetadata> quarantinedMetadata = new ArrayList<>(quarantinedFileIds.size());
        for (Long fileId : quarantinedFileIds) {
            final String keyName = metadataManager.retrieveKeyNameOfQuarantinedFile(fileId);
            quarantinedMetadata.add(new QuarantineMetadata(fileId, keyName));
        }
        return quarantinedMetadata;
    }

    /**
 * Checks if a specific file is quarantined by verifying its ID against
 * the list of all quarantined files.
 *
 * @param fileId The unique identifier of the file to check.
 * @return {@code true} if the file is quarantined, {@code false} otherwise.
 */
public final boolean isFileQuarantined(long fileId) {

    try {
        return metadataManager.getFileIdsOfAllQuarantinedFiles().contains(fileId);
    } catch (MetadataKeyNameRetrievalException e) {
        log.error("Failed to determine if the file is quarantined due to a metadata parsing error");
        return false;
    }
}

    /**
     * Handles the metadata saving process for a quarantined file.
     *
     * @param keyName The unique key name of the quarantined file.
     * @param fileId  The ID of the file that was quarantined.
     * @throws MetadataSaveException If there is an error saving metadata for the
     *                               quarantined file.
     */
    private void handleQuarantine(String keyName, Long fileId) throws MetadataSaveException {
        log.debug("Saving metadata for quarantined file. KeyName: {}, FileID: {}", keyName, fileId);
        metadataManager.saveMetadataForQuarantinedFile(keyName, fileId);
    }

    /**
     * Handles the metadata removal process for an unquarantined file.
     *
     * @param fileId The ID of the file that was unquarantined.
     * @throws MetadataRemoveException If there is an error removing metadata for
     *                                 the unquarantined file.
     */
    private void handleUnquarantine(Long fileId) throws MetadataRemoveException {
        log.debug("Removing metadata for unquarantined file. FileID: {}", fileId);
        metadataManager.removeMetadataForUnQuarantinedFile(fileId);
    }
}
