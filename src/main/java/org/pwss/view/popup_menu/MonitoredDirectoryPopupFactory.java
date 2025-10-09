package org.pwss.view.popup_menu;

import java.util.Optional;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.utils.StringConstants;
import org.pwss.view.popup_menu.listener.MonitoredDirectoryPopupListener;

/**
 * Factory class to create a context menu (popup menu) for monitored directories in a JTable.
 * The menu provides options to scan the directory, reset its baseline, and edit its details.
 */
public class MonitoredDirectoryPopupFactory {
    private final MonitoredDirectoryPopupListener listener;

    public MonitoredDirectoryPopupFactory(MonitoredDirectoryPopupListener listener) {
        this.listener = listener;
    }

    public JPopupMenu create(JTable table, int viewRow) {
        JPopupMenu menu = new JPopupMenu();

        int modelRow = table.convertRowIndexToModel(viewRow);
        MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) table.getModel();
        Optional<MonitoredDirectory> dirOpt = model.getDirectoryAt(modelRow);

        if (dirOpt.isEmpty()) return menu;
        MonitoredDirectory dir = dirOpt.get();

        // Scan this directory
        JMenuItem scanItem = new JMenuItem(StringConstants.MON_DIR_POPUP_START_SCAN);
        scanItem.addActionListener(e -> listener.onStartScan());

        // Reset baseline
        JMenuItem resetBaselineItem = getResetBaselineItem(dir);

        // Edit directory
        JMenuItem editDirectory = getEditDirectoryItem(dir);

        // Update note
        JMenuItem updateNoteItem = getUpdateNoteItem(dir);

        menu.add(scanItem);
        menu.add(editDirectory);
        menu.add(updateNoteItem);
        menu.addSeparator();
        menu.add(resetBaselineItem);

        return menu;
    }

    private JMenuItem getResetBaselineItem(MonitoredDirectory dir) {
        JMenuItem resetBaselineItem = new JMenuItem(StringConstants.MON_DIR_POPUP_RESET_BASELINE);
        resetBaselineItem.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    listener.getParentComponent(),
                    StringConstants.MON_DIR_POPUP_RESET_BASELINE_POPUP_MESSAGE + dir.path(),
                    StringConstants.MON_DIR_POPUP_RESET_BASELINE_POPUP_TITLE,
                    JOptionPane.WARNING_MESSAGE
            );

            try {
                if (input == null || input.isBlank()) {
                    listener.showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR_EMPTY_INPUT);
                    return;
                }
                listener.onResetBaseline(dir, Long.parseLong(input));
            } catch (NumberFormatException ex) {
                listener.showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR_INVALID);
            }
        });
        return resetBaselineItem;
    }

    private JMenuItem getEditDirectoryItem(MonitoredDirectory dir) {
        JMenuItem editItem = new JMenuItem(StringConstants.MON_DIR_POPUP_EDIT_DIR);
        editItem.addActionListener(e -> listener.onEditDirectory(dir));
        return editItem;
    }

    private JMenuItem getUpdateNoteItem(MonitoredDirectory dir) {
        JMenuItem updateNoteItem = new JMenuItem(StringConstants.MON_DIR_POPUP_UPDATE_NOTE);
        updateNoteItem.addActionListener(e -> {
            String newNote = JOptionPane.showInputDialog(
                listener.getParentComponent(),
                StringConstants.MON_DIR_POPUP_UPDATE_NOTE_POPUP_PREFIX + dir.path(),
                    StringConstants.MON_DIR_POPUP_UPDATE_NOTE,
                JOptionPane.PLAIN_MESSAGE
            );
            listener.onUpdateNote(dir, newNote);
        });
        return updateNoteItem;
    }
}

