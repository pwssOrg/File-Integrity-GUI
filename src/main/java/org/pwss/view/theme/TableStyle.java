package org.pwss.view.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
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
 * headers and rows drawn as separate rounded cards that light up under the
 * mouse and take an accent gradient with a coloured strip when selected,
 * tinted icon tiles in front of paths and coloured badges for states.
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
     * Width of the accent strip on the selected row, in pixels.
     */
    private static final int SELECTION_MARKER_WIDTH = 4;

    /**
     * Height of a row, including the gap between row cards.
     */
    public static final int ROW_HEIGHT = 46;

    /**
     * Space between a row card and the row edges: horizontal, vertical.
     */
    private static final int CARD_MARGIN_X = 8;
    private static final int CARD_MARGIN_Y = 3;
    private static final int CARD_ARC = 12;

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
        table.setRowHeight(ROW_HEIGHT);

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
        Color rest = Palette.mix(table.getForeground(), table.getBackground(), 0.045);
        if (selected) {
            return Palette.mix(Palette.accent(), table.getBackground(), 0.24);
        }
        if (isHovered(table, row)) {
            return Palette.mix(Palette.accent(), rest, 0.10);
        }
        return rest;
    }

    /**
     * Paints the part of a row card that falls into one cell. Together the
     * cells of a row form one rounded card, inset from the row edges, so rows
     * read as separate tiles. Selected rows get a horizontal accent gradient
     * and a coloured strip at their leading edge.
     *
     * @param g        the graphics of the cell renderer
     * @param table    the table
     * @param row      the view row of the cell
     * @param column   the view column of the cell
     * @param selected whether the row is selected
     * @param width    the cell width
     * @param height   the cell height
     */
    public static void paintRowCard(Graphics g, JTable table, int row, int column, boolean selected,
            int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(table.getBackground());
            g2.fillRect(0, 0, width, height);

            boolean first = column == 0;
            boolean last = column == table.getColumnCount() - 1;
            // Stretch the shape past the cell edges that join a neighbour, so
            // only the outer cells show rounded corners
            double x = first ? CARD_MARGIN_X : -CARD_ARC;
            double right = last ? width - CARD_MARGIN_X : width + CARD_ARC;
            RoundRectangle2D shape = new RoundRectangle2D.Double(x, CARD_MARGIN_Y, right - x,
                    height - 2 * CARD_MARGIN_Y, CARD_ARC, CARD_ARC);

            if (selected) {
                int cellX = table.getCellRect(row, column, true).x;
                Color strong = Palette.mix(Palette.accent(), table.getBackground(), 0.30);
                Color soft = Palette.mix(Palette.accent(), table.getBackground(), 0.12);
                g2.setPaint(new GradientPaint(-cellX, 0, strong, table.getWidth() - cellX, 0, soft));
            } else {
                g2.setColor(rowBackground(table, row, false));
            }
            g2.fill(shape);

            if (selected && first) {
                g2.clip(shape);
                g2.setColor(Palette.accent());
                g2.fillRect(CARD_MARGIN_X, 0, SELECTION_MARKER_WIDTH, height);
            }
        } finally {
            g2.dispose();
        }
    }

    private static boolean isHovered(JTable table, int row) {
        return Integer.valueOf(row).equals(table.getClientProperty(HOVER_ROW));
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
                    BorderFactory.createEmptyBorder(0, CARD_MARGIN_X + 14, 0, 12)));
            return this;
        }
    }

    /**
     * Body cell: formats the value and decorates it with an icon or renders it
     * as a badge, depending on what it is.
     */
    private static final class CellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        private final Icon folderIcon = AppIcons.tile(AppIcons.Glyph.FOLDER, () -> Palette.hue(Palette.TEAL), 26);
        private final Icon fileIcon = AppIcons.tile(AppIcons.Glyph.FILE, () -> Palette.hue(Palette.VIOLET), 26);
        private final Icon dateIcon = AppIcons.of(AppIcons.Glyph.CLOCK, 14, null);

        /**
         * Colour of the badge to paint instead of plain text, or null.
         */
        private Color badgeColor;
        private JTable table;
        private int row;
        private int column;
        private boolean selected;

        private CellRenderer() {
            setIconTextGap(10);
            setOpaque(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            // No per-cell focus frame: the whole row is the selection
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            this.table = table;
            this.row = row;
            this.column = column;
            this.selected = isSelected;
            setOpaque(false);
            // The selection is shown by the row card, so the text keeps its colour
            setForeground(table.getForeground());
            int leading = column == 0 ? CARD_MARGIN_X + 14 : 14;
            int trailing = column == table.getColumnCount() - 1 ? CARD_MARGIN_X + 12 : 12;
            setBorder(BorderFactory.createEmptyBorder(0, leading, 0, trailing));

            String columnName = table.getColumnName(column);
            Object shown = value;
            badgeColor = null;
            setIcon(null);

            if (value instanceof Date date) {
                shown = dateFormat.format(date);
                setIcon(dateIcon);
                setIconTextGap(8);
                setForeground(Palette.muted());
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
            if (getIcon() != dateIcon) {
                setIconTextGap(10);
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
            paintRowCard(g, table, row, column, selected, getWidth(), getHeight());
            if (badgeColor == null) {
                super.paintComponent(g);
            } else {
                paintBadge(g);
            }
        }

        /**
         * Paints the cell text as a pill: tinted fill, coloured text and a
         * leading dot.
         */
        private void paintBadge(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
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

                g2.setColor(new Color(badgeColor.getRed(), badgeColor.getGreen(), badgeColor.getBlue(), 48));
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
