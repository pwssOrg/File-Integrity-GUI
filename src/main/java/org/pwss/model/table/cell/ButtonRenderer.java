package org.pwss.model.table.cell;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.pwss.view.theme.TableStyle;

/**
 * A custom table cell renderer that renders a compact button, centred in the
 * table cell. The button looks disabled when the cell cannot be edited (i.e.
 * the action is not available for that row).
 */
public class ButtonRenderer extends JPanel implements TableCellRenderer {

    /**
     * Recommended width, in pixels, for a table column holding action buttons.
     */
    public static final int COLUMN_WIDTH = 140;

    /**
     * The button painted inside the cell.
     */
    private final JButton button;

    private JTable table;
    private int row;
    private int column;
    private boolean selected;

    /**
     * Constructs a new `ButtonRenderer`.
     */
    public ButtonRenderer() {
        super(new GridBagLayout());
        setOpaque(false);
        button = createCellButton("");
        add(button);
    }

    /**
     * Creates a compact button suited to sit inside a table row.
     *
     * @param text the text to display on the button
     * @return the button
     */
    static JButton createCellButton(String text) {
        JButton cellButton = new JButton(text);
        cellButton.setFocusable(false);
        cellButton.setMargin(new Insets(1, 10, 1, 10));
        return cellButton;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        button.setText((value == null) ? "" : value.toString());
        button.setEnabled(table.isCellEditable(row, column));
        this.table = table;
        this.row = row;
        this.column = column;
        this.selected = isSelected;
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (table != null) {
            TableStyle.paintRowCard(g, table, row, column, selected, getWidth(), getHeight());
        }
    }
}

