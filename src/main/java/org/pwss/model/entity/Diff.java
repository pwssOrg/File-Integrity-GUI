package org.pwss.model.entity;

/**
 * Represents the difference between two scan summaries: a baseline and an integrity failure.
 *
 * @param baseline      The baseline scan summary.
 * @param integrityFail The integrity failure scan summary.
 */
public record Diff(ScanSummary baseline, ScanSummary integrityFail) {
}
