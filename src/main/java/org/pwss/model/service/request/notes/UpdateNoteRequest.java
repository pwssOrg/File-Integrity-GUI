package org.pwss.model.service.request.notes;

/**
 * Record representing a request to update notes.
 *
 * @param noteId the ID of the note to be updated
 * @param text   the new text for the note
 */
public record UpdateNoteRequest(long noteId, String text) {
}
