package org.pwss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import org.pwss.exception.notes.RestoreNoteException;
import org.pwss.exception.notes.UpdateNoteException;
import org.pwss.model.request.notes.RestoreNoteRequest;
import org.pwss.model.request.notes.RestoreNoteType;
import org.pwss.model.request.notes.UpdateNoteRequest;
import org.pwss.service.network.Endpoint;
import org.pwss.service.network.PwssHttpClient;

public class NoteService {
    /**
     * An instance of the ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    public NoteService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Updates the notes for a specific note ID by sending a request to the NOTE_UPDATE endpoint.
     *
     * @param noteId   The ID of the note to be updated.
     * @param newNotes The new text for the note.
     * @return true if the update is successful, false otherwise.
     * @throws UpdateNoteException       If the attempt to update the notes fails due to various reasons such as invalid credentials, invalid note ID or content, or server error.
     * @throws JsonProcessingException   If an error occurs while processing JSON data.
     * @throws ExecutionException        If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException      If the thread executing the request is interrupted.
     */
    public boolean updateNotes(long noteId, String newNotes) throws UpdateNoteException, JsonProcessingException, ExecutionException, InterruptedException  {
        String body = objectMapper.writeValueAsString(new UpdateNoteRequest(noteId, newNotes));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.NOTE_UPDATE, body);
        return switch (response.statusCode()) {
            case 200 -> true;
            case 400 -> throw new UpdateNoteException("Update notes failed: Invalid note ID or note content.");
            case 401 -> throw new UpdateNoteException("Update notes failed: User not authorized to perform this action.");
            case 500 -> throw new UpdateNoteException("Update notes failed: An error occurred on the server while attempting to update the notes.");
            default -> false;
        };
    }

    /**
     * Restores the notes for a specific note ID to a previous version by sending a request to the NOTE_RESTORE endpoint.
     *
     * @param noteId          The ID of the note to be restored.
     * @param restoreNoteType The type of restoration to be performed (e.g., previous note, note before previous).
     * @return true if the restoration is successful, false otherwise.
     * @throws RestoreNoteException    If the attempt to restore the notes fails due to various reasons such as invalid credentials, invalid note ID or content, or server error.
     * @throws JsonProcessingException If an error occurs while processing JSON data.
     * @throws ExecutionException      If an error occurs during the asynchronous execution of the request.
     * @throws InterruptedException    If the thread executing the request is interrupted.
     */
    public boolean restoreNotes(long noteId, RestoreNoteType restoreNoteType) throws RestoreNoteException, JsonProcessingException, ExecutionException, InterruptedException {
        String body = objectMapper.writeValueAsString(new RestoreNoteRequest(noteId, restoreNoteType.toString()));
        HttpResponse<String> response = PwssHttpClient.getInstance().request(Endpoint.NOTE_RESTORE, body);
        return switch (response.statusCode()) {
            case 200 -> true;
            case 400 -> throw new RestoreNoteException("Restore notes failed: Invalid note ID or note content.");
            case 401 ->
                    throw new RestoreNoteException("Restore notes failed: User not authorized to perform this action.");
            case 500 ->
                    throw new RestoreNoteException("Restore notes failed: An error occurred on the server while attempting to restore the notes.");
            default -> false;
        };
    }
}
