package org.pwss.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.pwss.model.livefeed.ScanLogEntry;
import org.pwss.model.livefeed.ScanLogEntry.Kind;

/**
 * Turns the raw live feed text of the scanner into scan log entries.
 * <p>
 * The scanner sends its results as one run of text without separators, for
 * example {@code a.txt has not changed since the baseline scan ✅b.txt has
 * changed since the baseline scan ⚠️}. A result can be cut in two by a poll
 * (file name in one chunk, verdict in the next), so text that does not form a
 * complete result yet is kept and joined with the next chunk. Use one parser
 * per scan and call {@link #flush(LocalTime)} when the scan is over.
 */
public final class LiveFeedParser {

    /**
     * The endings that complete an entry in the live feed.
     */
    private static final Pattern TOKEN = Pattern.compile(
            "(?<ok> has not changed since the baseline scan ✅)"
                    + "|(?<changed> has changed since the baseline scan ⚠️?)"
                    + "|(?<big> with size \\((?<mb>\\d+) MB\\) is bigger than the user defined max limit)"
                    + "|(?<msg>--- Message: (?<text>.*?) ---)",
            Pattern.DOTALL);

    /**
     * Text received so far that is not a complete entry yet.
     */
    private String pending = "";

    /**
     * Parses a chunk of live feed text.
     *
     * @param chunk the raw text from the scanner, may be null or empty
     * @param time  the time to stamp the entries with
     * @return the entries completed by this chunk, oldest first
     */
    public List<ScanLogEntry> parse(String chunk, LocalTime time) {
        List<ScanLogEntry> entries = new ArrayList<>();
        if (chunk == null || chunk.isEmpty()) {
            return entries;
        }
        String text = pending + chunk;
        Matcher matcher = TOKEN.matcher(text);
        int consumed = 0;
        while (matcher.find()) {
            String before = text.substring(consumed, matcher.start()).strip();
            if (matcher.group("msg") != null) {
                addInfo(entries, before, time);
                addInfo(entries, matcher.group("text").strip(), time);
            } else if (matcher.group("big") != null) {
                entries.add(new ScanLogEntry(Kind.SKIPPED, before, matcher.group("mb") + " MB", time));
            } else {
                Kind kind = matcher.group("ok") != null ? Kind.UNCHANGED : Kind.CHANGED;
                entries.add(new ScanLogEntry(kind, before, "", time));
            }
            consumed = matcher.end();
        }
        pending = text.substring(consumed);
        return entries;
    }

    /**
     * Returns whatever text is still waiting to be completed, as plain messages.
     *
     * @param time the time to stamp the entries with
     * @return the remaining entries, empty if nothing was left over
     */
    public List<ScanLogEntry> flush(LocalTime time) {
        List<ScanLogEntry> entries = new ArrayList<>();
        addInfo(entries, pending, time);
        pending = "";
        return entries;
    }

    private static void addInfo(List<ScanLogEntry> entries, String text, LocalTime time) {
        for (String line : text.split("\\R")) {
            if (!line.isBlank()) {
                entries.add(ScanLogEntry.info(line.strip(), time));
            }
        }
    }
}
