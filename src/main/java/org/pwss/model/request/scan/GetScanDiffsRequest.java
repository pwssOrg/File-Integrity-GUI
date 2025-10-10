package org.pwss.model.request.scan;

public record GetScanDiffsRequest(long scanId, long limit, String sortField, boolean ascending) {
}
