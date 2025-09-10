package org.pwss.model.entity;

import java.util.Date;


public record MonitoredDirectory(long id, String path, boolean isActive, Time addedAt, Date lastScanned, Notes notes,
                                 boolean baselineEstablished, boolean includeSubdirectories) {
}
