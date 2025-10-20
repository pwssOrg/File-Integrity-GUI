package org.pwss.model.table.cell;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A custom table cell renderer that renders a button in a table cell.
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    /**
     * Constructs a new `ButtonRenderer` and sets the button to be opaque.
     */
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

