package org.pwss.model.request.scan_summary;

/**
 * Record representing a request to search for files with specific parameters.
 *
 * @param searchQuery The search query string to filter files.
 * @param limit       The maximum number of results to return.
 * @param sortField   The field by which to sort the results.
 * @param ascending   Whether the sorting should be in ascending order.
 */
public record GetFilesSearchRequest(String searchQuery, int limit, String sortField, boolean ascending) {
}
