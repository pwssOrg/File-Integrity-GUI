package org.pwss.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.pwss.exception.metadata.MetadataKeyNameRetrievalException;
import org.pwss.exception.metadata.MetadataRemoveException;
import org.pwss.exception.metadata.MetadataSaveException;
import org.slf4j.LoggerFactory;

/**
 * The MetadataManager class is responsible for managing metadata of quarantined
 * files.
 * It provides methods to save, retrieve and remove metadata associated with
 * file IDs.
 */
public final class MetadataManager {

    /**
     * Logger instance used for logging messages within this manager.
     */
    private final org.slf4j.Logger log;

    /**
     * The properties file used to store metadata about quarantined files.
     */
    private final String METADATA_FILE = "metadata.temp";

    /**
     * The full path to the metadata file for quarantined files
     */
    private final String METADATA_FULL_PATH = "app_storage" + File.separator + "metadata" + File.separator
            + METADATA_FILE;

    /**
     * The extension used to identify file IDs in metadata keys.
     */
    private final String FILE_ID_EXTENSION = ".id";

    /**
     * The extension used for encrypted files in the key name.
     */
    private final String ENC_EXTENSION = ".enc";

    /**
     * Constructs a new MetadataManager instance and initializes the logger.
     *
     * This constructor sets up an instance of {@link Logger} using SLF4J's
     * {@code LoggerFactory} to facilitate logging throughout this class. The
     * logger is specific to instances of this class, allowing for more flexible
     * logging configurations if needed in future implementations.
     */
    public MetadataManager() {
        this.log = LoggerFactory.getLogger(MetadataManager.class);
    }

    /**
     * Saves metadata for a quarantined file.
     *
     * @param keyName The name of the key.
     * @param fileId  The ID of the file.
     * @throws MetadataSaveException If an error occurs while saving the metadata.
     */
    public final void saveMetadataForQuarantinedFile(String keyName, Long fileId) throws MetadataSaveException {
        validateKeyNameAndFileId(keyName, fileId);
        try {
            saveMetadata(keyName, fileId);
        } catch (Exception exception) {
            log.debug("Failed to save metadata for quarantined file", exception);
            throw new MetadataSaveException(exception.getMessage());
        }
    }

    /**
     * Removes metadata for an unquarantined file. This method should be used when a
     * file unquarantine operation
     * on the backend server has executed successfully.
     * 
     * @param fileId The ID of the file.
     * @throws MetadataRemoveException If an error occurs while removing the
     *                                 metadata.
     */
    public final void removeMetadataForUnQuarantinedFile(Long fileId) throws MetadataRemoveException {
        validateFileId(fileId);
        try {
            removeMetadata(fileId);
        } catch (Exception exception) {
            log.debug("Failed to remove metadata for quarantined file", exception);
            throw new MetadataRemoveException(exception.getMessage());
        }
    }

    /**
     * Retrieves the key name of a quarantined file.
     *
     * Use this method when you need to retrieve the key name that you will send
     * to the local backend server when you need to unquarantine a file.
     * 
     * @param fileId The ID of the file.
     * @return The key name.
     * @throws MetadataKeyNameRetrievalException If an error occurs while retrieving
     *                                           the key name.
     */
    public final String retrieveKeyNameOfQuarantinedFile(Long fileId) throws MetadataKeyNameRetrievalException {
        try {
            Properties metadataProperties = loadMetadata();
            return getKeyNameFromProperties(metadataProperties, fileId);
        } catch (Exception e) {
            log.debug("Failed to retrieve key name from quarantined file", e);
            throw new MetadataKeyNameRetrievalException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of file IDs for all quarantined files by processing the
     * metadata properties.
     *
     * @return A List of Long values representing the file IDs of all quarantined
     *         files.
     * @throws MetadataKeyNameRetrievalException If an error occurs during the
     *                                           retrieval process, such as invalid
     *                                           format or parsing errors.
     */
    public final List<Long> getFileIdsOfAllQuarantinedFiles() throws MetadataKeyNameRetrievalException {

        List<Long> fileIdList = new LinkedList<>();
        try {
            Properties metadataProperties = loadMetadata();

            for (String key : metadataProperties.stringPropertyNames()) {

                fileIdList.add(retrieveFileIdFromKeyName(key));

            }

        } catch (Exception e) {
            log.debug("Failed to retrieve key name when getting all quarantined files", e);
            throw new MetadataKeyNameRetrievalException(e.getMessage(), e);
        }

        return fileIdList;
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
            return getFileIdsOfAllQuarantinedFiles().contains(fileId);
        } catch (MetadataKeyNameRetrievalException e) {
            log.error("Failed to determine if the file is quarantined due to a metadata parsing error");
            return false;
        }
    }

    /**
     * Loads metadata properties from the metadata file.
     *
     * @return The loaded Properties object.
     * @throws Exception If an error occurs while loading the metadata.
     */
    private final Properties loadMetadata() throws Exception {
        Properties props = new Properties();
        File metadataFile = new File(METADATA_FULL_PATH);
        if (metadataFile.exists()) {
            try (FileInputStream fis = new FileInputStream(metadataFile)) {
                props.load(fis);
            }
        }
        return props;
    }

    /**
     * Saves metadata for a quarantined file.
     *
     * @param keyName The name of the key.
     * @param fileId  The ID of the file.
     * @throws Exception If an error occurs while saving the metadata.
     */
    private final void saveMetadata(String keyName, Long fileId) throws Exception {
        Properties props = loadMetadata();
        props.setProperty(getFileIdMetadataKey(fileId), keyName);

        try (FileOutputStream fos = new FileOutputStream(METADATA_FULL_PATH)) {
            props.store(fos, null);
        } catch (Exception exception) {
            log.debug("Could not set options in Meta Data file", exception);
            throw new MetadataSaveException("Failed to save metadata", exception);
        }
    }

    /**
     * Removes metadata for a quarantined file.
     *
     * @param fileId The ID of the file.
     * @throws Exception If an error occurs while removing the metadata.
     */
    private final void removeMetadata(Long fileId) throws Exception {
        Properties props = loadMetadata();
        props.remove(getFileIdMetadataKey(fileId));

        try (FileOutputStream fos = new FileOutputStream(METADATA_FULL_PATH)) {
            props.store(fos, null);
        } catch (Exception exception) {
            log.debug("Failed to remove metadata for quarantined file with id {}\nException {}", fileId, exception);
            throw new MetadataRemoveException("Failed to remove metadata", exception);
        }
    }

    /**
     * Constructs the metadata key for a given file ID.
     *
     * @param fileId The ID of the file.
     * @return The metadata key.
     */
    private final String getFileIdMetadataKey(Long fileId) {

        return String.valueOf(fileId) + FILE_ID_EXTENSION;
    }

    /**
     * Validates the provided key name and file ID.
     *
     * @param keyName The name of the key.
     * @param fileId  The ID of the file.
     * @throws IllegalArgumentException If the key name or file ID is invalid.
     */
    private final void validateKeyNameAndFileId(String keyName, Long fileId) throws IllegalArgumentException {
        if (keyName == null || keyName.isEmpty()) {
            log.debug("Keyname is null or empty");
            throw new IllegalArgumentException("Invalid key name");
        }
        validateFileId(fileId);
    }

    /**
     * Validates the provided file ID.
     *
     * @param fileId The ID of the file.
     * @throws IllegalArgumentException If the file ID is invalid.
     */
    private final void validateFileId(Long fileId) throws IllegalArgumentException {
        if (fileId == null) {
            log.debug("File ID is null");
            throw new IllegalArgumentException("Invalid file ID");
        }
    }

    /**
     * Retrieves the file ID from a given key name by splitting the string based on
     * the predefined file ID
     * extension.
     *
     * @param keyName The name of the metadata key which includes the file ID as
     *                part of its string.
     * @return The extracted file ID as a Long value.
     * @throws MetadataKeyNameRetrievalException If an error occurs during the
     *                                           retrieval process, such as invalid
     *                                           format or parsing errors.
     */
    private final Long retrieveFileIdFromKeyName(String keyName) throws MetadataKeyNameRetrievalException {
        try {
            String fileIdString = keyName.split(FILE_ID_EXTENSION)[0];
            return Long.valueOf(fileIdString);
        } catch (Exception exception) {

            throw new MetadataKeyNameRetrievalException("Could not retrieve file id when splitting up String");
        }
    }

    /**
     * Retrieves the key name from the metadata properties for a given file ID.
     *
     * @param metadataProperties The metadata properties object.
     * @param fileId             The ID of the file.
     * @return The key name.
     * @throws MetadataKeyNameRetrievalException If an error occurs while retrieving
     *                                           the key name.
     */
    private final String getKeyNameFromProperties(Properties metadataProperties, Long fileId)
            throws MetadataKeyNameRetrievalException {
        final String keyNameWithExtension = metadataProperties.getProperty(getFileIdMetadataKey(fileId));
        if (keyNameWithExtension == null || !keyNameWithExtension.endsWith(ENC_EXTENSION)) {
            throw new MetadataKeyNameRetrievalException(
                    "Failed to retrieve key name from metadata file due to extensions errors");
        }
        return keyNameWithExtension.split(ENC_EXTENSION)[0];
    }

}
