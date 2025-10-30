package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryByIdException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.monitored_directory.NewMonitoredDirectoryBaselineException;
import org.pwss.exception.monitored_directory.NewMonitoredDirectoryException;
import org.pwss.exception.monitored_directory.UpdateMonitoredDirectoryException;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.request.monitored_directory.GetDirectoryByIdRequest;
import org.pwss.model.request.monitored_directory.NewBaselineRequest;
import org.pwss.model.request.monitored_directory.NewDirectoryRequest;
import org.pwss.model.request.monitored_directory.UpdateDirectoryRequest;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;

public class MonitoredDirectoryService {
        /**
         * An instance of the ObjectMapper used for JSON serialization and
         * deserialization.
         */
        private final ObjectMapper objectMapper;

        public MonitoredDirectoryService() {
                this.objectMapper = new ObjectMapper();
        }

        /**
         * Retrieves all monitored directories by sending a request to the
         * MONITORED_DIRECTORY_ALL endpoint.
         *
         * @return A list of MonitoredDirectory objects representing all monitored
         *         directories.
         * @throws MonitoredDirectoryGetAllException If the retrieval attempt fails due
         *                                           to various reasons such as invalid
         *                                           credentials or server error.
         * @throws ExecutionException                If an error occurs during the
         *                                           asynchronous execution of the
         *                                           request.
         * @throws InterruptedException              If the thread executing the request
         *                                           is interrupted.
         * @throws JsonProcessingException           If an error occurs while
         *                                           deserializing the response body to
         *                                           MonitoredDirectory objects.
         */
        public List<MonitoredDirectory> getAllDirectories() throws MonitoredDirectoryGetAllException,
                        ExecutionException, InterruptedException, JsonProcessingException {
                HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MONITORED_DIRECTORY_ALL,
                                null);

                return switch (response.statusCode()) {
                        case 200 -> List.of(objectMapper.readValue(response.body(), MonitoredDirectory[].class));
                        case 401 ->
                                throw new MonitoredDirectoryGetAllException(
                                                "Get all monitored directories failed: User not authorized to perform this action.");
                        case 500 ->
                                throw new MonitoredDirectoryGetAllException(
                                                "Get all monitored directories failed: An error occurred on the server while attempting to retrieve the monitored directories.");
                        default -> List.of();
                };
        }

        /**
         * Retrieves a monitored directory by its ID by sending a request to the
         * MONITORED_DIRECTORY_BY_ID endpoint.
         *
         * @param id The ID of the monitored directory to retrieve.
         * @return A MonitoredDirectory object representing the monitored directory with
         *         the specified ID.
         * @throws MonitoredDirectoryByIdException If the retrieval attempt fails due to
         *                                         various reasons such as invalid
         *                                         credentials, monitored directory not
         *                                         found, or server error.
         * @throws ExecutionException              If an error occurs during the
         *                                         asynchronous execution of the
         *                                         request.
         * @throws InterruptedException            If the thread executing the request
         *                                         is interrupted.
         * @throws JsonProcessingException         If an error occurs while serializing
         *                                         the request body or deserializing the
         *                                         response body.
         */
        public MonitoredDirectory getDirectoryById(int id) throws MonitoredDirectoryByIdException, ExecutionException,
                        InterruptedException, JsonProcessingException {
                String body = objectMapper.writeValueAsString(new GetDirectoryByIdRequest(id));
                HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.MONITORED_DIRECTORY_BY_ID,
                                body);

                return switch (response.statusCode()) {
                        case 200 -> objectMapper.readValue(response.body(), MonitoredDirectory.class);
                        case 401 ->
                                throw new MonitoredDirectoryByIdException(
                                                "Get monitored directory by ID failed: User not authorized to perform this action.");
                        case 404 ->
                                throw new MonitoredDirectoryByIdException(
                                                "Get monitored directory by ID failed: Monitored directory not found.");
                        case 500 ->
                                throw new MonitoredDirectoryByIdException(
                                                "Get monitored directory by ID failed: An error occurred on the server while attempting to retrieve the monitored directory.");
                        default -> null;
                };
        }

        /**
         * Creates a new monitored directory by sending a request to the
         * MONITORED_DIRECTORY_CREATE endpoint.
         *
         * @param path                  The path of the directory to be monitored.
         * @param includeSubdirectories A boolean indicating whether subdirectories
         *                              should be included.
         * @param isActive              A boolean indicating whether the monitored
         *                              directory should be active.
         * @return A `MonitoredDirectory` object representing the newly created
         *         monitored directory.
         * @throws NewMonitoredDirectoryException If the creation attempt fails due to
         *                                        invalid input, unauthorized access, or
         *                                        server error.
         * @throws ExecutionException             If an error occurs during the
         *                                        asynchronous execution of the request.
         * @throws InterruptedException           If the thread executing the request is
         *                                        interrupted.
         * @throws JsonProcessingException        If an error occurs while serializing
         *                                        the request body or deserializing the
         *                                        response body.
         */
        public MonitoredDirectory createNewMonitoredDirectory(String path, boolean includeSubdirectories,
                        boolean isActive) throws NewMonitoredDirectoryException, ExecutionException,
                        InterruptedException, JsonProcessingException {
                String body = objectMapper
                                .writeValueAsString(new NewDirectoryRequest(path, includeSubdirectories, isActive));
                HttpResponse<String> response = PwssHttpClient.getInstance()
                                .request(Endpoint.MONITORED_DIRECTORY_CREATE, body);

                return switch (response.statusCode()) {
                        case 200 -> objectMapper.readValue(response.body(), MonitoredDirectory.class);
                        case 400 ->
                                throw new NewMonitoredDirectoryException(
                                                "Create monitored directory failed: Invalid directory path or request body.",
                                                body);
                        case 401 ->
                                throw new NewMonitoredDirectoryException(
                                                "Create monitored directory failed: User not authorized to perform this action.");
                        case 500 ->
                                throw new NewMonitoredDirectoryException(
                                                "Create monitored directory failed: An error occurred on the server while attempting to create the monitored directory.",
                                                body);
                        default -> null;
                };
        }

        /**
         * Updates an existing monitored directory by sending a request to the
         * MONITORED_DIRECTORY_UPDATE endpoint.
         *
         * @param id             The ID of the monitored directory to update.
         * @param isActive       The new active status of the monitored directory.
         * @param notes          Any notes associated with the monitored directory.
         * @param includeSubDirs Whether to include subdirectories in monitoring.
         * @return `true` if the update was successful, otherwise false.
         * @throws UpdateMonitoredDirectoryException If the update attempt fails due to
         *                                           invalid input, unauthorized access,
         *                                           or server error.
         * @throws ExecutionException                If an error occurs during the
         *                                           asynchronous execution of the
         *                                           request.
         * @throws InterruptedException              If the thread executing the request
         *                                           is interrupted.
         * @throws JsonProcessingException           If an error occurs while
         *                                           serializing the request body.
         */
        public boolean updateMonitoredDirectory(long id, boolean isActive, String notes, boolean includeSubDirs)
                        throws UpdateMonitoredDirectoryException, JsonProcessingException, ExecutionException,
                        InterruptedException {
                String body = objectMapper
                                .writeValueAsString(new UpdateDirectoryRequest(id, isActive, notes, includeSubDirs));
                HttpResponse<String> response = PwssHttpClient.getInstance()
                                .request(Endpoint.MONITORED_DIRECTORY_UPDATE, body);

                return switch (response.statusCode()) {
                        case 200 -> true;
                        case 401 ->
                                throw new UpdateMonitoredDirectoryException(
                                                "Update monitored directory failed: User not authorized to perform this action.");
                        case 422 ->
                                throw new UpdateMonitoredDirectoryException(
                                                "Update monitored directory failed: Unprocessable entity - invalid input data.");
                        case 500 ->
                                throw new UpdateMonitoredDirectoryException(
                                                "Update monitored directory failed: An error occurred on the server while attempting to update the monitored directory.");
                        default -> false;
                };
        }

        /**
         * Creates a new baseline for a monitored directory by sending a request to the
         * MONITORED_DIRECTORY_NEW_BASELINE endpoint.
         *
         * @param id           The ID of the monitored directory for which to create a
         *                     new baseline.
         * @param endpointCode Code for verifying the action of creating a new baseline.
         * @return `true` if the baseline creation was successful, otherwise false.
         * @throws NewMonitoredDirectoryBaselineException If the baseline creation
         *                                                attempt fails due to
         *                                                unauthorized access, monitored
         *                                                directory not found, or server
         *                                                error.
         * @throws ExecutionException                     If an error occurs during the
         *                                                asynchronous execution of the
         *                                                request.
         * @throws InterruptedException                   If the thread executing the
         *                                                request is interrupted.
         * @throws JsonProcessingException                If an error occurs while
         *                                                serializing the request body.
         */
        public boolean newMonitoredDirectoryBaseline(long id, long endpointCode)
                        throws NewMonitoredDirectoryBaselineException, ExecutionException, InterruptedException,
                        JsonProcessingException {
                String body = objectMapper.writeValueAsString(new NewBaselineRequest(id, endpointCode));
                HttpResponse<String> response = PwssHttpClient.getInstance()
                                .request(Endpoint.MONITORED_DIRECTORY_NEW_BASELINE, body);

                return switch (response.statusCode()) {
                        case 200 -> true;
                        case 401 ->
                                throw new NewMonitoredDirectoryBaselineException(
                                                "Create new baseline failed: User not authorized to perform this action.");
                        case 404 ->
                                throw new NewMonitoredDirectoryBaselineException(
                                                "Create new baseline failed: Monitored directory not found.");
                        case 500 ->
                                throw new NewMonitoredDirectoryBaselineException(
                                                "Create new baseline failed: An error occurred on the server while attempting to create the new baseline.");
                        default -> false;
                };
        }
}
