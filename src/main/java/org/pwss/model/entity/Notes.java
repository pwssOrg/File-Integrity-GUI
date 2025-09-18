package org.pwss.model.entity;

/**
 * Represents notes associated with a scan or file.
 *
 * @param id            The unique identifier for the notes entry.
 * @param notes         The current notes.
 * @param prevNotes     The previous notes.
 * @param prevPrevNotes The notes before the previous ones.
 * @param time          The timestamp of when the notes were last updated.
 */
public record Notes(long id, String notes, String prevNotes, String prevPrevNotes, Time time) {
}
