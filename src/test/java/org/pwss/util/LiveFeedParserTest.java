package org.pwss.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.pwss.model.livefeed.ScanLogEntry;
import org.pwss.model.livefeed.ScanLogEntry.Kind;

class LiveFeedParserTest {

    private static final LocalTime TIME = LocalTime.of(12, 0);
    private static final String OK = " has not changed since the baseline scan ✅";
    private static final String CHANGED = " has changed since the baseline scan ⚠️";

    @Test
    void parsesResultsThatAreRunTogether() {
        List<ScanLogEntry> entries = new LiveFeedParser().parse(
                "a.txt" + OK + "b b.txt" + CHANGED
                        + "big.iso with size (120 MB) is bigger than the user defined max limit" + "c.txt" + OK,
                TIME);

        assertEquals(List.of(
                new ScanLogEntry(Kind.UNCHANGED, "a.txt", "", TIME),
                new ScanLogEntry(Kind.CHANGED, "b b.txt", "", TIME),
                new ScanLogEntry(Kind.SKIPPED, "big.iso", "120 MB", TIME),
                new ScanLogEntry(Kind.UNCHANGED, "c.txt", "", TIME)), entries);
    }

    @Test
    void parsesScannerMessages() {
        List<ScanLogEntry> entries = new LiveFeedParser().parse(
                "--- Message: Only diffs will be shown from now on. ---a.txt" + CHANGED, TIME);

        assertEquals(List.of(
                ScanLogEntry.info("Only diffs will be shown from now on.", TIME),
                new ScanLogEntry(Kind.CHANGED, "a.txt", "", TIME)), entries);
    }

    @Test
    void joinsResultsSplitAcrossChunks() {
        LiveFeedParser parser = new LiveFeedParser();

        assertEquals(List.of(new ScanLogEntry(Kind.UNCHANGED, "a.txt", "", TIME)),
                parser.parse("a.txt" + OK + "b.txt", TIME));
        assertEquals(List.of(new ScanLogEntry(Kind.CHANGED, "b.txt", "", TIME)), parser.parse(CHANGED, TIME));
        assertTrue(parser.flush(TIME).isEmpty());
    }

    @Test
    void flushReturnsUnrecognisedText() {
        LiveFeedParser parser = new LiveFeedParser();

        assertTrue(parser.parse("something else", TIME).isEmpty());
        assertEquals(List.of(ScanLogEntry.info("something else", TIME)), parser.flush(TIME));
        assertTrue(parser.parse(null, TIME).isEmpty());
        assertTrue(parser.flush(TIME).isEmpty());
    }
}
