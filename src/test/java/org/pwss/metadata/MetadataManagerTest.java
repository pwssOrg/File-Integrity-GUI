package org.pwss.metadata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.pwss.exception.metadata.MetadataKeyNameRetrievalException;
import org.pwss.exception.metadata.MetadataRemoveException;
import org.pwss.exception.metadata.MetadataSaveException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetadataManagerTest {

    private MetadataManager metadataManager = new MetadataManager();
    private long mockFileId = 6688;

    @Order(1)
    @Test
    void insertMetadataTest() throws MetadataSaveException {

        final String keyNameFromMockBackend = "C_drive__.server_test.hej.txt.enc";

        Assertions.assertDoesNotThrow(() -> {
            metadataManager.saveMetadataForQuarantinedFile(keyNameFromMockBackend,
                    mockFileId);
        });

    }

    @Order(2)
    @Test
    void retrieveKeyNameTest() throws MetadataKeyNameRetrievalException {

        final String EXPECTED = "C_drive__.server_test.hej.txt";
        final String ACTUAL = metadataManager.retrieveKeyNameOfQuarantinedFile(mockFileId);
        Assertions.assertEquals(EXPECTED, ACTUAL);
    }

    @Order(3)
    @Test
    void retrieveFileIdsFromAllMetadataKeysTest() throws MetadataKeyNameRetrievalException {

        final Long EXPECTED = mockFileId;
        final Long ACTUAL = metadataManager.getFileIdsOfAllQuarantinedFiles().get(0);
        Assertions.assertEquals(EXPECTED, ACTUAL);

    }

    @Order(4)
    @Test
    void removeMetadataTest() throws MetadataRemoveException {

        Assertions.assertDoesNotThrow(() -> {
            metadataManager.removeMetadataForUnQuarantinedFile(mockFileId);

        });

    }
}
