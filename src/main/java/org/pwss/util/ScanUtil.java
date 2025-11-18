package org.pwss.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import org.pwss.model.entity.Scan;

/**
 * Utility class for constructing scan-related messages.
 */
public final class ScanUtil {

    /**
     * Prefix string used in scan completion messages indicating the number of
     * differences found.
     */
    private static final String SCAN_COMPLETED_DIFFS_PREFIX = "Scan completed with ";

    /**
     * Suffix string used in scan completion messages providing additional
     * information about viewing details.
     */
    private static final String SCAN_COMPLETED_DIFFS_SUFFIX = " differences found.\nDo you wish to see the details?\nYou can always view results later in the recent scans table.";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ScanUtil() {
        // Prevent instantiation
    }

    /**
     * Constructs a scan completion message with the specified number of differences
     * found.
     *
     * @param diffNumber The number of differences found during the scan.
     * @return A string containing the complete scan message.
     */
    public static String constructDiffMessageString(long diffNumber) {
        return SCAN_COMPLETED_DIFFS_PREFIX + diffNumber + SCAN_COMPLETED_DIFFS_SUFFIX;
    }

    /**
     * Filters the provided list of scans to return a list containing only distinct
     * scans based on their monitored directory IDs.
     *
     * @param scans The list of scans to filter.
     * @return A list of scans with distinct monitored directory IDs.
     */
    public static List<Scan> getScansDistinctByDirectory(List<Scan> scans) {
        return scans.stream()
                .filter(distinctByKey(scan -> scan.monitoredDirectory().id()))
                .toList();
    }

    // Helper method to create a predicate for distinct filtering based on a key extractor
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
