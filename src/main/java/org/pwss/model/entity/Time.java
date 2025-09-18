package org.pwss.model.entity;

import java.util.Date;

/**
 * Represents a timestamp with creation and update dates.
 *
 * @param id      The unique identifier for the time entry.
 * @param created The date when the entry was created.
 * @param updated The date when the entry was last updated.
 */
public record Time(long id, Date created, Date updated) {
}
