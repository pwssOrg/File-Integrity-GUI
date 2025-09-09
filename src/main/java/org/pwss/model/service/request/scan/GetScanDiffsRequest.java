package org.pwss.model.service.request.scan;

public record GetScanDiffsRequest(long scanId, long limit, String sortField, boolean ascending) {
}
