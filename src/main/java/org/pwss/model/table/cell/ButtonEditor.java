package org.pwss.model.table.cell;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.pwss.model.table.DiffTableModel;
import org.pwss.utils.OSUtils;

/**
 * A custom table cell editor that displays a JButton in a JTable cell.
 */
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private JTable table;
    private boolean clicked;

    /**
     * Constructs a ButtonEditor with the specified button text.
     *
     * @param text the text to display on the button
     */
    public ButtonEditor(String text) {
        button = new JButton(text);
        button.setOpaque(true);
        button.setFocusable(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setMargin(new Insets(0, 0, 0, 0));

        // When clicked, stop editing (which triggers getCellEditorValue)
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked && table.getModel() instanceof DiffTableModel model) {
            int modelRow = table.convertRowIndexToModel(table.getEditingRow());
            model.getDiffAt(modelRow).ifPresent(diff ->
                    JOptionPane.showMessageDialog(
                            button,
                            OSUtils.describeOS(),
                            OSUtils.getOSName(), ///  FOR TESTING not supposed to be here :p
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );
        }
        clicked = false;
        return null;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
