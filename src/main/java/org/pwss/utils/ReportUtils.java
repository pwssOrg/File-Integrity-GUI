package org.pwss.utils;

import org.pwss.model.entity.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportUtils {

    /**
     * Formats a ScanSummary into a human-readable string.
     *
     * @param summary the ScanSummary to format
     * @return a formatted string representation of the ScanSummary
     */
    public static String formatSummary(ScanSummary summary) {
        StringBuilder sb = new StringBuilder();

        Scan scan = summary.scan();
        File file = summary.file();
        Checksum checksum = summary.checksum();


        String filePath = file != null ? file.path() : "<unknown>";
        long fileSize = file != null ? file.size() : 0L;
        String mtime = file != null ? formatDate(file.mtime()) : "";

        String scanStatus = scan != null ? scan.status() : "<unknown>";
        String scanTime = (scan != null && scan.scanTime() != null)
                ? formatDate(scan.scanTime().created())
                : "";

        String notes = (scan != null && scan.notes() != null)
                ? scan.notes().notes()
                : "No notes";
        String prevNotes = (scan != null && scan.notes() != null)
                ? scan.notes().prevNotes()
                : null;

        String checksumSha256 = checksum != null ? checksum.checksumSha256() : "";
        String checksumSha3 = checksum != null ? checksum.checksumSha3() : "";
        String checksumBlake2b = checksum != null ? checksum.checksumBlake2b() : "";

        sb.append("==============================\n")
                .append("📂 File: ").append(filePath).append("\n")
                .append("┣ Size: ").append(fileSize).append(" bytes\n")
                .append("┣ Last Modified: ").append(mtime).append("\n\n")
                .append("🔍 Scan Status: ").append(scanStatus).append("\n")
                .append("┣ Scan Time: ").append(scanTime).append("\n\n")
                .append("📝 Notes: ").append(notes).append("\n");

        if (prevNotes != null) {
            sb.append("┣ Previous Notes: ").append(prevNotes).append("\n");
        }

        sb.append("\n🔑 Checksums:\n")
                .append("┣ ").append(checksumSha256).append("\n")
                .append("┣ ").append(checksumSha3).append("\n")
                .append("┗ ").append(checksumBlake2b).append("\n")
                .append("==============================\n");


        return sb.toString();
    }

    /**
     * Formats a Diff into a human-readable string.
     *
     * @param diff the Diff to format
     * @return a formatted string representation of the Diff
     */
    public static String formatDiff(Diff diff) {
        StringBuilder sb = new StringBuilder();

        ScanSummary baseline = diff.baseline();
        ScanSummary integrityFail = diff.integrityFail();
        Date timestamp = diff.time() != null ? diff.time().created() : null;

        String formattedTime = formatDate(timestamp);

        sb.append("==============================\n")
                .append("🆚 Diff Report\n")
                .append("┣ Recorded At: ").append(formattedTime).append("\n\n")
                // Baseline Summary
                .append("📌 Baseline Scan:\n")
                .append(formatSummary(baseline)).append("\n\n")
                // Integrity Failure Summary
                .append("⚠️ Integrity Failure:\n")
                .append(formatSummary(integrityFail)).append("\n");

        return sb.toString();
    }


    private static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
