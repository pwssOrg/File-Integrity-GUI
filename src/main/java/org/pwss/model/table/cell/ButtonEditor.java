package org.pwss.model.table.cell;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/**
 * A custom table cell editor that displays a JButton in a JTable cell.
 */
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    /**
     * The button component used as the editor in the table cell.
     */
    private final JButton button;
    /**
     * The panel that centres the button inside the table cell.
     */
    private final JPanel cell;
    /**
     * The JTable instance that is being edited.
     */
    private JTable table;
    /**
     * A flag indicating whether the button was clicked.
     */
    private boolean clicked;
    /**
     * The listener that handles button click events in the table cell.
     */
    private CellButtonListener listener;

    /**
     * Constructs a ButtonEditor with the specified button text.
     *
     * @param text the text to display on the button
     */
    public ButtonEditor(String text, CellButtonListener listener) {
        // Same compact button as the renderer, so the cell does not change when clicked
        button = ButtonRenderer.createCellButton(text);
        cell = new JPanel(new GridBagLayout());
        cell.add(button);

        this.listener = listener;

        // When clicked, stop editing (which triggers getCellEditorValue)
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        cell.setBackground(table.getSelectionBackground());
        return cell;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            clicked = false;
            if (listener != null && table != null) {
                int row = table.getEditingRow();
                int column = table.getEditingColumn();
                listener.onCellButtonClicked(row, column);
            }
        }
        return null;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
