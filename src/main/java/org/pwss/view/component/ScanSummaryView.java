package org.pwss.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.pwss.model.entity.Checksum;
import org.pwss.model.entity.File;
import org.pwss.model.entity.Scan;
import org.pwss.model.entity.ScanSummary;
import org.pwss.view.theme.AppIcons;
import org.pwss.view.theme.AppIcons.Glyph;
import org.pwss.view.theme.AppLookAndFeel;
import org.pwss.view.theme.Palette;
import org.pwss.view.theme.UiStyle;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * Shows one scan summary as a set of cards: the file and scan result at the
 * top, size and dates as tiles, the scan notes and the three checksums, each
 * with its own colour and a copy button.
 * <p>
 * Replaces the plain text report in the scan summary panes. Install it with
 * {@link #installIn(JScrollPane)} and update it with {@link #show(ScanSummary)}
 * and {@link #clear()}.
 */
public final class ScanSummaryView extends JPanel implements Scrollable {

    private static final int GAP = 12;
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private ScanSummary summary;
    private int laidOutWidth = -1;

    /**
     * Creates an empty view.
     */
    public ScanSummaryView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        rebuild();
    }

    /**
     * Shows the view inside a scroll pane, in place of whatever it showed before.
     *
     * @param scrollPane the scroll pane to fill
     */
    public void installIn(JScrollPane scrollPane) {
        scrollPane.setViewportView(this);
        scrollPane.getViewport().setBackground(getBackground());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Shows a scan summary.
     *
     * @param summary the summary to show, or null to show the empty state
     */
    public void show(ScanSummary summary) {
        this.summary = summary;
        rebuild();
    }

    /**
     * Clears the view back to its empty state.
     */
    public void clear() {
        show(null);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        // Wrapped text only knows its height once it knows its width, so lay
        // out a second time whenever the width changes
        if (getWidth() != laidOutWidth) {
            laidOutWidth = getWidth();
            SwingUtilities.invokeLater(() -> {
                invalidateTree(this);
                revalidate();
                repaint();
            });
        }
    }

    private static void invalidateTree(java.awt.Container container) {
        container.invalidate();
        for (Component child : container.getComponents()) {
            if (child instanceof java.awt.Container c) {
                invalidateTree(c);
            }
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        // Colours come from the theme, so rebuild when it changes
        if (getLayout() instanceof BoxLayout) {
            rebuild();
        }
    }

    private void rebuild() {
        removeAll();
        setBackground(Palette.background());
        if (summary == null) {
            add(emptyState());
        } else {
            addCard(header());
            addCard(stats());
            addCard(notes());
            addCard(checksums());
        }
        add(Box.createVerticalGlue());
        revalidate();
        repaint();
        scrollRectToVisible(new Rectangle(0, 0, 1, 1));
    }

    private void addCard(JComponent card) {
        if (getComponentCount() > 0) {
            add(Box.createVerticalStrut(GAP));
        }
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(card);
    }

    // ---- Sections ----

    private JComponent emptyState() {
        JLabel label = new JLabel("Select a scan to see its details",
                AppIcons.of(Glyph.FILE, 28, Palette.muted()), SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setIconTextGap(10);
        label.setForeground(Palette.muted());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        label.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        return label;
    }

    private JComponent header() {
        File file = summary.file();
        Scan scan = summary.scan();
        String status = scan != null && scan.status() != null ? scan.status() : "UNKNOWN";
        Color statusColor = statusColor(status);

        Card card = Card.gradient(new BorderLayout(14, 0), Palette::accent, () -> statusColor).fixedHeight();
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel tile = new JLabel(AppIcons.tile(Glyph.FILE, Palette::accent, 44));
        tile.setVerticalAlignment(SwingConstants.TOP);
        card.add(tile, BorderLayout.LINE_START);

        String name = file != null ? orElse(file.basename(), file.path()) : null;
        JLabel title = new JLabel(orElse(name, "Unknown file"));
        title.setFont(UiStyle.font("h3.font"));
        title.setToolTipText(file != null ? file.path() : null);

        JLabel path = new JLabel(file != null ? orElse(file.directory(), file.path()) : "");
        path.setForeground(Palette.muted());
        path.setToolTipText(file != null ? file.path() : null);

        JPanel pills = row(FlowLayout.LEADING, 0);
        pills.add(new Pill(prettyStatus(status), statusColor, statusIcon(statusColor)));
        if (scan != null && scan.isBaselineScan()) {
            pills.add(Box.createHorizontalStrut(6));
            pills.add(new Pill("Baseline", Palette.accent(), null));
        }

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        for (JComponent c : new JComponent[] { title, path, pills }) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        text.add(title);
        text.add(Box.createVerticalStrut(2));
        text.add(path);
        text.add(Box.createVerticalStrut(10));
        text.add(pills);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JComponent stats() {
        File file = summary.file();
        Scan scan = summary.scan();
        Date scanned = scan != null && scan.scanTime() != null ? scan.scanTime().created() : null;

        JPanel grid = new JPanel(new GridLayout(1, 3, GAP, 0)) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        grid.setOpaque(false);
        JComponent size = statTile(Glyph.LIST, Palette.hue(0), "Size",
                file != null ? humanSize(file.size()) : "-");
        if (file != null) {
            size.setToolTipText(String.format("%,d bytes", file.size()));
        }
        grid.add(size);
        grid.add(statTile(Glyph.CLOCK, Palette.hue(1), "Last modified", formatDate(file != null ? file.mtime() : null)));
        grid.add(statTile(Glyph.SCAN, Palette.hue(2), "Scanned", formatDate(scanned)));
        return grid;
    }

    private JComponent statTile(Glyph glyph, Color color, String caption, String value) {
        Card tile = new Card(new BorderLayout(10, 0));
        tile.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel icon = new JLabel(AppIcons.tile(glyph, color, 32));
        icon.setVerticalAlignment(SwingConstants.CENTER);
        tile.add(icon, BorderLayout.LINE_START);

        JLabel captionLabel = new JLabel(caption.toUpperCase(Locale.ROOT));
        captionLabel.setFont(UiStyle.font("small.font"));
        captionLabel.setForeground(Palette.muted());
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UiStyle.font("semibold.font"));
        valueLabel.setToolTipText(value);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        text.add(captionLabel);
        text.add(valueLabel);
        tile.add(text, BorderLayout.CENTER);
        return tile;
    }

    private JComponent notes() {
        Scan scan = summary.scan();
        String notes = scan != null && scan.notes() != null ? scan.notes().notes() : null;
        String prevNotes = scan != null && scan.notes() != null ? scan.notes().prevNotes() : null;

        Card card = section("Notes", Glyph.INFO, Palette.hue(1));
        card.add(wrappingText(orElse(notes, "No notes"), null, notes == null ? Palette.muted() : null));
        if (prevNotes != null && !prevNotes.isBlank()) {
            card.add(Box.createVerticalStrut(10));
            JLabel previous = new JLabel("PREVIOUS");
            previous.setFont(UiStyle.font("small.font"));
            previous.setForeground(Palette.muted());
            previous.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(previous);
            card.add(Box.createVerticalStrut(2));
            card.add(wrappingText(prevNotes, null, Palette.muted()));
        }
        return card;
    }

    private JComponent checksums() {
        Checksum checksum = summary.checksum();
        Card card = section("Checksums", Glyph.CHECK, AppLookAndFeel.okColor());
        card.add(hashRow("SHA-256", Palette.hue(0), checksum != null ? checksum.checksumSha256() : null));
        card.add(Box.createVerticalStrut(8));
        card.add(hashRow("SHA-3", Palette.hue(3), checksum != null ? checksum.checksumSha3() : null));
        card.add(Box.createVerticalStrut(8));
        card.add(hashRow("BLAKE2b", Palette.hue(2), checksum != null ? checksum.checksumBlake2b() : null));
        return card;
    }

    private JComponent hashRow(String algorithm, Color color, String hash) {
        boolean present = hash != null && !hash.isBlank();
        Card card = new Card(new BorderLayout(10, 0), () -> Palette.mix(color, Palette.surface(), 0.08), null)
                .arc(10).strip(() -> color).fixedHeight();
        card.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 6));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        Pill badge = new Pill(algorithm, color, null);
        badge.setPreferredSize(new Dimension(Math.max(badge.getPreferredSize().width, 82),
                badge.getPreferredSize().height));
        JPanel badgeHolder = row(FlowLayout.LEADING, 0);
        badgeHolder.add(badge);
        card.add(badgeHolder, BorderLayout.LINE_START);

        card.add(wrappingText(present ? hash : "Not available", UiStyle.font("monospaced.font"),
                present ? null : Palette.muted()), BorderLayout.CENTER);

        if (present) {
            JButton copy = new JButton(AppIcons.of(Glyph.COPY, 14, null));
            copy.setToolTipText("Copy " + algorithm);
            copy.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
            copy.setFocusable(false);
            copy.addActionListener(e -> {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(hash), null);
                copy.setIcon(AppIcons.of(Glyph.CHECK, 14, AppLookAndFeel.okColor()));
                copy.setToolTipText("Copied");
                Timer reset = new Timer(1400, t -> {
                    copy.setIcon(AppIcons.of(Glyph.COPY, 14, null));
                    copy.setToolTipText("Copy " + algorithm);
                });
                reset.setRepeats(false);
                reset.start();
            });
            JPanel copyHolder = row(FlowLayout.TRAILING, 0);
            copyHolder.add(copy);
            card.add(copyHolder, BorderLayout.LINE_END);
        }
        return card;
    }

    // ---- Building blocks ----

    /**
     * A card with a coloured heading, laid out top to bottom.
     */
    private Card section(String title, Glyph glyph, Color color) {
        Card card = new Card(null).fixedHeight();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));
        JLabel heading = new JLabel(title, AppIcons.of(glyph, 16, color), SwingConstants.LEADING);
        heading.setIconTextGap(8);
        heading.setFont(UiStyle.font("semibold.font"));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(heading);
        card.add(Box.createVerticalStrut(10));
        return card;
    }

    /**
     * Read-only, selectable text that wraps to the available width.
     */
    private static JTextArea wrappingText(String text, Font font, Color foreground) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(font == null);
        area.setOpaque(false);
        area.setBorder(null);
        area.setMargin(new Insets(0, 0, 0, 0));
        area.setFont(font != null ? font : UIManager.getFont("Label.font"));
        if (foreground != null) {
            area.setForeground(foreground);
        }
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    private static JPanel row(int align, int gap) {
        JPanel panel = new JPanel(new FlowLayout(align, gap, 0));
        panel.setOpaque(false);
        return panel;
    }

    // ---- Formatting ----

    private static String formatDate(Date date) {
        return date == null ? "-" : new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    private static String humanSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        String[] units = { "KB", "MB", "GB", "TB" };
        double value = bytes;
        int unit = -1;
        while (value >= 1024 && unit < units.length - 1) {
            value /= 1024;
            unit++;
        }
        return String.format(value >= 100 ? "%.0f %s" : "%.1f %s", value, units[unit]);
    }

    private static String prettyStatus(String status) {
        String lower = status.toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static String orElse(String value, String fallback) {
        return value != null && !value.isBlank() ? value : fallback;
    }

    // ---- Colours ----

    private static Color statusColor(String status) {
        String upper = status.toUpperCase(Locale.ROOT);
        if (upper.equals("COMPLETED")) {
            return AppLookAndFeel.okColor();
        }
        if (upper.contains("FAIL") || upper.contains("ERROR")) {
            return AppLookAndFeel.errorColor();
        }
        return AppLookAndFeel.warnColor();
    }

    private static Icon statusIcon(Color color) {
        Glyph glyph = color.equals(AppLookAndFeel.okColor()) ? Glyph.CHECK : Glyph.WARNING;
        return AppIcons.of(glyph, 13, color);
    }

    // ---- Scrollable: follow the viewport width, scroll vertically ----

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return Math.max(16, visibleRect.height - 32);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // Fill the viewport when the cards are shorter than it, so no bare
        // viewport background shows below them
        return getParent() instanceof javax.swing.JViewport viewport
                && viewport.getHeight() > getPreferredSize().height;
    }
}
