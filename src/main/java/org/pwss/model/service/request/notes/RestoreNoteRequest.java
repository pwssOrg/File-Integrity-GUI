package org.pwss.model.service.request.notes;

/**
 * A record representing a request to restore a note.
 *
 * @param noteId      the ID of the note to be restored
 * @param restoreNote historical note to revert to (PREV_NOTE or PREV_PREV_NOTE)
 */
public record RestoreNoteRequest(long noteId, RestoreNoteType restoreNote) {
}
