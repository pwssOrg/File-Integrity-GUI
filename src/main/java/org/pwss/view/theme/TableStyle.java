package org.pwss.view.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The shared look of every table in the application: quiet upper-case
 * headers, striped rows that highlight under the mouse, an accent marker on
 * the selected row, icons in front of paths and coloured badges for states.
 */
public final class TableStyle {

    /**
     * Pattern used to show dates and times in tables.
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * Client property holding the row currently under the mouse.
     */
    private static final String HOVER_ROW = "App.hoverRow";

    /**
     * Columns whose values are directories.
     */
    private static final Set<String> DIRECTORY_COLUMNS = Set.of("Directory", "Path");

    /**
     * Columns whose values are files.
     */
    private static final Set<String> FILE_COLUMNS = Set.of("File name", "File path", "Quarantined file path");

    /**
     * Column whose values are scan states.
     */
    private static final String STATUS_COLUMN = "Status";

    /**
     * Width of the accent marker on the selected row, in pixels.
     */
    private static final int SELECTION_MARKER_WIDTH = 3;

    private TableStyle() {
    }

    /**
     * Applies the table look to the given table. The look survives model
     * changes, so this only has to be called once per table.
     *
     * @param table the table to style
     */
    public static void install(JTable table) {
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setRowHeight(38);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());

        TableCellRenderer cellRenderer = new CellRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setDefaultRenderer(Date.class, cellRenderer);
        table.setDefaultRenderer(Number.class, cellRenderer);
        table.setDefaultRenderer(Boolean.class, cellRenderer);

        MouseAdapter hoverTracker = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setHoverRow(table, table.rowAtPoint(e.getPoint()));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHoverRow(table, -1);
            }
        };
        table.addMouseListener(hoverTracker);
        table.addMouseMotionListener(hoverTracker);
    }

    /**
     * Returns the background a cell in the given row should be painted with,
     * taking selection, the mouse position and row striping into account.
     *
     * @param table    the table
     * @param row      the view row of the cell
     * @param selected whether the row is selected
     * @return the background colour
     */
    public static Color rowBackground(JTable table, int row, boolean selected) {
        if (selected) {
            return table.getSelectionBackground();
        }
        if (Integer.valueOf(row).equals(table.getClientProperty(HOVER_ROW))) {
            return color("App.tableHoverBackground", table.getBackground());
        }
        return row % 2 == 1 ? color("App.tableStripeBackground", table.getBackground()) : table.getBackground();
    }

    private static void setHoverRow(JTable table, int row) {
        Object previous = table.getClientProperty(HOVER_ROW);
        if (!Integer.valueOf(row).equals(previous)) {
            table.putClientProperty(HOVER_ROW, row);
            table.repaint();
        }
    }

    private static Color color(String key, Color fallback) {
        Color color = UIManager.getColor(key);
        return color != null ? color : fallback;
    }

    /**
     * Header cell: small upper-case caption with a hairline underneath.
     */
    private static final class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setText(value == null ? "" : value.toString().toUpperCase(Locale.ROOT));
            setHorizontalAlignment(SwingConstants.LEADING);
            Font base = UiStyle.font("semibold.font");
            setFont(base.deriveFont(base.getSize2D() - 2f));
            setForeground(color("App.mutedForeground", table.getForeground()));
            setBackground(table.getBackground());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0,
                            color("TableHeader.bottomSeparatorColor", table.getGridColor())),
                    BorderFactory.createEmptyBorder(0, 14, 0, 12)));
            return this;
        }
    }

    /**
     * Body cell: formats the value and decorates it with an icon or renders it
     * as a badge, depending on what it is.
     */
    private static final class CellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        private final Icon folderIcon;
        private final Icon fileIcon;

        /**
         * Colour of the badge to paint instead of plain text, or null.
         */
        private Color badgeColor;
        private boolean selectionMarker;

        private CellRenderer() {
            Color muted = color("App.mutedForeground", Color.GRAY);
            folderIcon = AppIcons.of(AppIcons.Glyph.FOLDER, 16, muted);
            fileIcon = AppIcons.of(AppIcons.Glyph.FILE, 16, muted);
            setIconTextGap(10);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            // No per-cell focus frame: the whole row is the selection
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            setBackground(rowBackground(table, row, isSelected));
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 12));
            selectionMarker = isSelected && column == 0;

            String columnName = table.getColumnName(column);
            Object shown = value;
            badgeColor = null;
            setIcon(null);

            if (value instanceof Date date) {
                shown = dateFormat.format(date);
            } else if (value instanceof Boolean flag) {
                shown = flag ? "Yes" : "No";
                badgeColor = flag ? AppLookAndFeel.okColor() : color("App.mutedForeground", Color.GRAY);
            } else if (value instanceof String text && !text.isEmpty()) {
                if (STATUS_COLUMN.equals(columnName)) {
                    shown = capitalize(text);
                    badgeColor = statusColor(text);
                } else if ("Yes".equals(text)) {
                    badgeColor = AppLookAndFeel.okColor();
                } else if ("No".equals(text)) {
                    badgeColor = AppLookAndFeel.warnColor();
                } else if (DIRECTORY_COLUMNS.contains(columnName)) {
                    setIcon(folderIcon);
                } else if (FILE_COLUMNS.contains(columnName)) {
                    setIcon(fileIcon);
                }
            }
            setText(shown == null ? "" : shown.toString());
            return this;
        }

        private static Color statusColor(String status) {
            return switch (status.toUpperCase(Locale.ROOT)) {
                case "COMPLETED", "SUCCESS", "FINISHED" -> AppLookAndFeel.okColor();
                case "FAILED", "ERROR" -> AppLookAndFeel.errorColor();
                default -> AppLookAndFeel.warnColor();
            };
        }

        private static String capitalize(String text) {
            String lower = text.replace('_', ' ').toLowerCase(Locale.ROOT);
            return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (badgeColor == null) {
                super.paintComponent(g);
            } else {
                paintBadge(g);
            }
            if (selectionMarker) {
                g.setColor(color("Component.accentColor", getForeground()));
                g.fillRect(0, 0, SELECTION_MARKER_WIDTH, getHeight());
            }
        }

        /**
         * Paints the cell text as a pill: tinted fill, coloured text and a
         * leading dot.
         */
        private void paintBadge(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Object textHint = UIManager.get(RenderingHints.KEY_TEXT_ANTIALIASING);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        textHint != null ? textHint : RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font base = UiStyle.font("semibold.font");
                g2.setFont(base.deriveFont(base.getSize2D() - 1f));
                FontMetrics metrics = g2.getFontMetrics();
                Insets insets = getInsets();
                int dot = 6;
                int padding = 10;
                int height = metrics.getHeight() + 6;
                int width = Math.min(getWidth() - insets.left - 4,
                        metrics.stringWidth(getText()) + dot + 6 + 2 * padding);
                int x = insets.left;
                int y = (getHeight() - height) / 2;

                g2.setColor(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 40));
                g2.fillRoundRect(x, y, width, height, height, height);
                g2.setColor(badgeColor);
                g2.fillOval(x + padding, y + (height - dot) / 2, dot, dot);
                g2.clipRect(x, y, width - padding / 2, height);
                g2.drawString(getText(), x + padding + dot + 6, y + (height - metrics.getHeight()) / 2
                        + metrics.getAscent());
            } finally {
                g2.dispose();
            }
        }
    }
}
