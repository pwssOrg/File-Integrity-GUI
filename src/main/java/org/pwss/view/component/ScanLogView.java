package org.pwss.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.pwss.model.livefeed.ScanLogEntry;
import org.pwss.model.livefeed.ScanLogEntry.Kind;
import org.pwss.view.theme.AppIcons;
import org.pwss.view.theme.AppLookAndFeel;
import org.pwss.view.theme.UiStyle;

/**
 * The scan log: a live list of what the running scan found, one row per file,
 * with running totals and a filter above it.
 * <p>
 * The view keeps following the newest entry as long as it is scrolled to the
 * bottom. Only the most recent entries are kept, and unchanged files are
 * dropped first so that changed and skipped files stay in the log.
 */
public final class ScanLogView {

    /**
     * The maximum number of entries kept in the log.
     */
    private static final int MAX_ENTRIES = 2000;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LogModel model = new LogModel();
    private final LogList list = new LogList();
    private final JPanel header = new JPanel(new BorderLayout(8, 0));
    private final JLabel unchangedLabel = new JLabel();
    private final JLabel changedLabel = new JLabel();
    private final JLabel skippedLabel = new JLabel();
    private final JCheckBox hideUnchanged = new JCheckBox("Hide unchanged");

    private int unchanged;
    private int changed;
    private int skipped;
    private boolean scanning;

    /**
     * Creates an empty scan log.
     */
    public ScanLogView() {
        Color muted = mutedColor();
        unchangedLabel.setIcon(AppIcons.dot(AppLookAndFeel.okColor()));
        changedLabel.setIcon(AppIcons.dot(AppLookAndFeel.warnColor()));
        skippedLabel.setIcon(AppIcons.dot(muted));

        JPanel totals = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        totals.setOpaque(false);
        for (JLabel label : new JLabel[] { unchangedLabel, changedLabel, skippedLabel }) {
            label.setIconTextGap(6);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
            totals.add(label);
        }
        hideUnchanged.setOpaque(false);
        hideUnchanged.addActionListener(e -> {
            model.setHideUnchanged(hideUnchanged.isSelected());
            scrollToEnd();
        });

        header.setBackground(list.getBackground());
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Component.borderColor")),
                BorderFactory.createEmptyBorder(8, 14, 8, 10)));
        header.add(totals, BorderLayout.CENTER);
        header.add(hideUnchanged, BorderLayout.LINE_END);

        list.setModel(model);
        list.setCellRenderer(new RowRenderer());
        list.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                list.setCellRenderer(new RowRenderer());
            }
        });
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.getActionMap().put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copySelection();
            }
        });
        updateTotals();
    }

    /**
     * Shows the log inside a scroll pane, with the totals pinned above the rows.
     *
     * @param scrollPane the scroll pane to fill
     */
    public void installIn(JScrollPane scrollPane) {
        scrollPane.setViewportView(list);
        scrollPane.setColumnHeaderView(header);
    }

    /**
     * Adds entries to the end of the log.
     *
     * @param entries the new entries, oldest first
     */
    public void append(List<ScanLogEntry> entries) {
        if (entries.isEmpty()) {
            return;
        }
        boolean follow = isAtEnd();
        for (ScanLogEntry entry : entries) {
            switch (entry.kind()) {
                case UNCHANGED -> unchanged++;
                case CHANGED -> changed++;
                case SKIPPED -> skipped++;
                default -> {
                }
            }
        }
        List<ScanLogEntry> selected = list.getSelectedValuesList();
        model.add(entries);
        restoreSelection(selected);
        updateTotals();
        if (follow) {
            scrollToEnd();
        }
    }

    private void restoreSelection(List<ScanLogEntry> selected) {
        if (selected.isEmpty()) {
            return;
        }
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            if (selected.contains(model.getElementAt(i))) {
                indices.add(i);
            }
        }
        list.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Removes all entries and resets the totals.
     */
    public void clear() {
        unchanged = 0;
        changed = 0;
        skipped = 0;
        model.clear();
        updateTotals();
    }

    /**
     * Tells whether the log holds no entries at all.
     *
     * @return true if nothing has been logged since the log was last cleared
     */
    public boolean isEmpty() {
        return model.isEmpty();
    }

    /**
     * Tells the log whether a scan is running, which decides what an empty log
     * says.
     *
     * @param scanning true while a scan is running
     */
    public void setScanning(boolean scanning) {
        if (this.scanning != scanning) {
            this.scanning = scanning;
            list.repaint();
        }
    }

    private void updateTotals() {
        unchangedLabel.setText(String.format("%,d unchanged", unchanged));
        changedLabel.setText(String.format("%,d changed", changed));
        skippedLabel.setText(String.format("%,d skipped", skipped));
        skippedLabel.setVisible(skipped > 0);
    }

    private boolean isAtEnd() {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, list);
        if (scrollPane == null) {
            return true;
        }
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        return bar.getValue() + bar.getVisibleAmount() >= bar.getMaximum() - 40;
    }

    private void scrollToEnd() {
        // Wait for the list to lay out the new rows
        SwingUtilities.invokeLater(() -> {
            int last = model.getSize() - 1;
            if (last >= 0) {
                list.ensureIndexIsVisible(last);
            }
        });
    }

    private void copySelection() {
        StringBuilder text = new StringBuilder();
        for (ScanLogEntry entry : list.getSelectedValuesList()) {
            text.append(TIME_FORMAT.format(entry.time())).append("  ").append(entry.describe()).append('\n');
        }
        if (!text.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text.toString()), null);
        }
    }

    private String emptyText() {
        if (!model.isEmpty()) {
            return "Only unchanged files so far";
        }
        return scanning ? "Waiting for the first results..." : "Nothing logged";
    }

    private static Color mutedColor() {
        Color color = UIManager.getColor("App.mutedForeground");
        return color != null ? color : UIManager.getColor("Label.disabledForeground");
    }

    /**
     * Blends two colours.
     */
    private static Color mix(Color color, Color base, double amount) {
        return new Color(
                (int) Math.round(color.getRed() * amount + base.getRed() * (1 - amount)),
                (int) Math.round(color.getGreen() * amount + base.getGreen() * (1 - amount)),
                (int) Math.round(color.getBlue() * amount + base.getBlue() * (1 - amount)));
    }

    /**
     * The list of rows. It always takes the width of its scroll pane (long file
     * names are shortened instead of scrolled) and explains itself when empty.
     */
    private final class LogList extends JList<ScanLogEntry> {

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getModel().getSize() > 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                g2.setColor(mutedColor());
                String text = emptyText();
                FontMetrics metrics = g2.getFontMetrics();
                g2.drawString(text, (getWidth() - metrics.stringWidth(text)) / 2, 28 + metrics.getAscent());
            } finally {
                g2.dispose();
            }
        }
    }

    /**
     * Holds all entries and the subset that passes the filter.
     */
    private static final class LogModel extends AbstractListModel<ScanLogEntry> {
        private final List<ScanLogEntry> all = new ArrayList<>();
        private List<ScanLogEntry> visible = new ArrayList<>();
        private boolean hideUnchanged;

        @Override
        public int getSize() {
            return visible.size();
        }

        @Override
        public ScanLogEntry getElementAt(int index) {
            return visible.get(index);
        }

        boolean isEmpty() {
            return all.isEmpty();
        }

        void add(List<ScanLogEntry> entries) {
            all.addAll(entries);
            if (all.size() > MAX_ENTRIES) {
                trim();
                rebuild();
                return;
            }
            int first = visible.size();
            for (ScanLogEntry entry : entries) {
                if (accepts(entry)) {
                    visible.add(entry);
                }
            }
            if (visible.size() > first) {
                fireIntervalAdded(this, first, visible.size() - 1);
            }
        }

        void clear() {
            all.clear();
            rebuild();
        }

        void setHideUnchanged(boolean hideUnchanged) {
            if (this.hideUnchanged != hideUnchanged) {
                this.hideUnchanged = hideUnchanged;
                rebuild();
            }
        }

        private boolean accepts(ScanLogEntry entry) {
            return !hideUnchanged || entry.kind() != Kind.UNCHANGED;
        }

        /**
         * Drops the oldest entries, unchanged files first.
         */
        private void trim() {
            int[] excess = { all.size() - (MAX_ENTRIES - 100) };
            all.removeIf(entry -> excess[0] > 0 && entry.kind() == Kind.UNCHANGED && excess[0]-- > 0);
            if (excess[0] > 0) {
                all.subList(0, excess[0]).clear();
            }
        }

        private void rebuild() {
            int oldSize = visible.size();
            visible = new ArrayList<>(all.stream().filter(this::accepts).toList());
            if (oldSize > 0) {
                fireIntervalRemoved(this, 0, oldSize - 1);
            }
            if (!visible.isEmpty()) {
                fireIntervalAdded(this, 0, visible.size() - 1);
            }
        }
    }

    /**
     * Paints a row: an icon, the file name, the result and the time. Messages
     * get a row of their own kind, with text that wraps.
     */
    private final class RowRenderer implements ListCellRenderer<ScanLogEntry> {
        private static final int GAP = 10;
        private static final Insets PADDING = new Insets(7, 14, 7, 12);

        private final Color muted = mutedColor();
        private final Icon unchangedIcon = AppIcons.of(AppIcons.Glyph.CHECK, 16, AppLookAndFeel.okColor());
        private final Icon changedIcon = AppIcons.of(AppIcons.Glyph.WARNING, 16, AppLookAndFeel.warnColor());
        private final Icon skippedIcon = AppIcons.of(AppIcons.Glyph.SKIP, 16, muted);
        private final Icon infoIcon = AppIcons.of(AppIcons.Glyph.INFO, 16, muted);

        private final JPanel fileRow = new JPanel(new BorderLayout(GAP, 0));
        private final JLabel fileIcon = new JLabel();
        private final JLabel name = new JLabel();
        private final JLabel status = new JLabel();
        private final JLabel time = new JLabel();

        private final JPanel infoRow = new JPanel(new BorderLayout(GAP, 0));
        private final JTextArea infoText = new JTextArea();
        private final JLabel infoTime = new JLabel();

        private RowRenderer() {
            Box trailing = Box.createHorizontalBox();
            trailing.add(status);
            trailing.add(Box.createHorizontalStrut(GAP));
            trailing.add(time);
            fileRow.setBorder(BorderFactory.createEmptyBorder(PADDING.top, PADDING.left, PADDING.bottom, PADDING.right));
            fileRow.add(fileIcon, BorderLayout.LINE_START);
            fileRow.add(name, BorderLayout.CENTER);
            fileRow.add(trailing, BorderLayout.LINE_END);

            JLabel icon = new JLabel(infoIcon);
            icon.setVerticalAlignment(SwingConstants.TOP);
            infoTime.setVerticalAlignment(SwingConstants.TOP);
            infoText.setLineWrap(true);
            infoText.setWrapStyleWord(true);
            infoText.setOpaque(false);
            infoText.setBorder(null);
            infoText.setMargin(new Insets(0, 0, 0, 0));
            infoText.setFont(UIManager.getFont("Label.font"));
            infoRow.setBorder(BorderFactory.createEmptyBorder(PADDING.top, PADDING.left, PADDING.bottom, PADDING.right));
            infoRow.add(icon, BorderLayout.LINE_START);
            infoRow.add(infoText, BorderLayout.CENTER);
            infoRow.add(infoTime, BorderLayout.LINE_END);

            time.setFont(UiStyle.font("small.font"));
            infoTime.setFont(UiStyle.font("small.font"));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ScanLogEntry> list, ScanLogEntry entry,
                                                      int index, boolean selected, boolean focused) {
            Color background = list.getBackground();
            if (selected) {
                background = list.getSelectionBackground();
            } else if (entry.kind() == Kind.CHANGED) {
                background = mix(AppLookAndFeel.warnColor(), background, 0.10);
            }
            Color foreground = selected ? list.getSelectionForeground() : list.getForeground();
            Color secondary = selected ? foreground : muted;
            String timeText = TIME_FORMAT.format(entry.time());

            if (entry.kind() == Kind.INFO) {
                infoRow.setBackground(background);
                infoRow.setToolTipText(null);
                infoText.setForeground(secondary);
                infoText.setText(entry.subject());
                infoTime.setForeground(secondary);
                infoTime.setText(timeText);
                // Tell the text how wide it is, so that it knows how tall it wraps
                int rowWidth = list.getWidth() > 0 ? list.getWidth() : 420;
                int textWidth = rowWidth - PADDING.left - PADDING.right - infoIcon.getIconWidth() - 2 * GAP
                        - infoTime.getPreferredSize().width;
                infoText.setSize(Math.max(80, textWidth), Short.MAX_VALUE);
                return infoRow;
            }

            fileRow.setBackground(background);
            fileRow.setToolTipText(entry.describe());
            name.setForeground(foreground);
            name.setText(entry.subject().isEmpty() ? "(unknown file)" : entry.subject());
            time.setForeground(secondary);
            time.setText(timeText);
            status.setFont(list.getFont());
            switch (entry.kind()) {
                case CHANGED -> {
                    fileIcon.setIcon(changedIcon);
                    status.setText("Changed");
                    status.setFont(UiStyle.font("semibold.font"));
                    status.setForeground(selected ? foreground : AppLookAndFeel.warnColor());
                }
                case SKIPPED -> {
                    fileIcon.setIcon(skippedIcon);
                    status.setText("Skipped · " + entry.detail());
                    status.setForeground(secondary);
                }
                default -> {
                    fileIcon.setIcon(unchangedIcon);
                    status.setText("Unchanged");
                    status.setForeground(secondary);
                }
            }
            return fileRow;
        }
    }
}
