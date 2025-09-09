package org.pwss.model.entity;

public record Notes(long id, String notes, String prevNotes, String prevPrevNotes, Time time) {
}
