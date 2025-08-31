package org.pwss.model.entity;

import java.util.Date;

/**
 * Represents a file in the filesystem.
 *
 * @param id        Unique identifier for the file.
 * @param path      Full filesystem path of the file.
 * @param basename  Base name of the file (name without directory path).
 * @param directory Directory path where the file is located.
 * @param size      Size of the file in bytes.
 * @param mTime     Last modification time of the file.
 */
public record File(long id, String path, String basename, String directory, long size, Date mTime) {
}
