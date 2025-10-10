package org.pwss.view.popup_menu;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.request.notes.RestoreNoteType;
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

        // Show note
        JMenuItem showNoteItem = getShowNoteItem(dir);

        // Update note
        JMenuItem updateNoteItem = getUpdateNoteItem(dir);

        // Restore note
        JMenuItem restoreNoteItem = getRestoreNoteItem(dir);

        // Assemble menu
        menu.add(scanItem);
        menu.add(editDirectory);
        menu.addSeparator();
        menu.add(showNoteItem);
        menu.add(updateNoteItem);
        menu.add(restoreNoteItem);
        menu.addSeparator();
        menu.add(resetBaselineItem);

        return menu;
    }

    /**
     * Creates a menu item for editing the details of a monitored directory.
     *
     * @param dir the monitored directory for which to create the menu item
     * @return the JMenuItem for editing the directory
     */
    private JMenuItem getEditDirectoryItem(MonitoredDirectory dir) {
        JMenuItem editItem = new JMenuItem(StringConstants.MON_DIR_POPUP_EDIT_DIR);
        editItem.addActionListener(e -> listener.onEditDirectory(dir));
        return editItem;
    }

    /**
     * Creates a menu item for showing the note of a monitored directory.
     *
     * @param dir the monitored directory for which to create the menu item
     * @return the JMenuItem for showing the note
     */
    private JMenuItem getShowNoteItem(MonitoredDirectory dir) {
        JMenuItem showNoteItem = new JMenuItem(StringConstants.MON_DIR_POPUP_SHOW_NOTE);
        showNoteItem.addActionListener(e -> {
            String note = dir.notes().notes();
            if (note == null || note.isBlank()) {
                note = StringConstants.MON_DIR_POPUP_NO_NOTE;
            }
            JOptionPane.showMessageDialog(
                    listener.getParentComponent(),
                    note,
                    StringConstants.MON_DIR_POPUP_SHOW_NOTE,
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        return showNoteItem;
    }

    /**
     * Creates a menu item for updating the note of a monitored directory.
     *
     * @param dir the monitored directory for which to create the menu item
     * @return the JMenuItem for updating the note
     */
    private JMenuItem getUpdateNoteItem(MonitoredDirectory dir) {
        JMenuItem updateNoteItem = new JMenuItem(StringConstants.MON_DIR_POPUP_UPDATE_NOTE);
        updateNoteItem.addActionListener(e -> {
            // Label for directory path
            JLabel label = new JLabel(StringConstants.MON_DIR_POPUP_UPDATE_NOTE_POPUP_PREFIX + dir.path());
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Text area for note input
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            // Scroll pane with only vertical scrolling
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(label);
            panel.add(Box.createVerticalStrut(5));
            panel.add(scrollPane);

            // Show confirm dialog
            int result = JOptionPane.showConfirmDialog(
                    listener.getParentComponent(),
                    panel,
                    StringConstants.MON_DIR_POPUP_UPDATE_NOTE,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // User cancelled
            if (result != JOptionPane.OK_OPTION)
                return;

            String newNote = textArea.getText();
            // Handle note update
            listener.onUpdateNote(dir, newNote);
        });

        return updateNoteItem;
    }

    /**
     * Creates a menu item for restoring a previous note of a monitored directory.
     *
     * @param dir the monitored directory for which to create the menu item
     * @return the JMenuItem for restoring a previous note
     */
    private JMenuItem getRestoreNoteItem(MonitoredDirectory dir) {
        JMenuItem restoreNoteItem = new JMenuItem(StringConstants.MON_DIR_POPUP_RESTORE_NOTE);
        restoreNoteItem.addActionListener(e -> {
            String prev = dir.notes().prevNotes();
            String prevPrev = dir.notes().prevPrevNotes();

            boolean hasPrev = prev != null && !prev.isEmpty();
            boolean hasPrevPrev = prevPrev != null && !prevPrev.isEmpty();

            // If no previous notes exist, show info dialog
            if (!hasPrev && !hasPrevPrev) {
                JOptionPane.showMessageDialog(
                        listener.getParentComponent(),
                        StringConstants.MON_DIR_POPUP_RESTORE_NO_NOTE_FALLBACK + dir.path(),
                        StringConstants.MON_DIR_POPUP_RESTORE_NOTE,
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Build the dialog message dynamically
            StringBuilder message = new StringBuilder();
            message.append(StringConstants.MON_DIR_POPUP_RESTORE_NOTE_POPUP_PREFIX)
                    .append(dir.path())
                    .append("\n\n");

            List<String> options = new ArrayList<>();

            // Always add previous note if it exists
            if (hasPrev) {
                message.append(StringConstants.MON_DIR_POPUP_INFO_PREV_NOTE)
                        .append(prev)
                        .append("\n\n");
                options.add(StringConstants.MON_DIR_POPUP_RESTORE_NOTE_PREV);
            }

            // Add previous-previous note only if it exists
            if (hasPrevPrev) {
                message.append(StringConstants.MON_DIR_POPUP_INFO_PREV_PREV_NOTE)
                        .append(prevPrev)
                        .append("\n\n");
                options.add(StringConstants.MON_DIR_POPUP_RESTORE_NOTE_PREV_PREV);
            }

            // Always add cancel
            options.add(StringConstants.GENERIC_CANCEL);

            // Show dialog
            int choice = JOptionPane.showOptionDialog(
                    listener.getParentComponent(),
                    message.toString(),
                    StringConstants.MON_DIR_POPUP_RESTORE_NOTE,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options.toArray(),
                    options.getFirst()
            );

            // Handle result
            if (choice == JOptionPane.CLOSED_OPTION || choice == options.size() - 1) {
                return; // Cancel or closed
            }

            // Map choices back to restore types
            if (hasPrev && choice == 0) {
                listener.onRestoreNote(dir, RestoreNoteType.PREV_NOTE);
            } else if (hasPrevPrev && choice == 1) {
                listener.onRestoreNote(dir, RestoreNoteType.PREV_PREV_NOTE);
            }
        });

        return restoreNoteItem;
    }

    /**
     * Creates a menu item for restoring the note of a monitored directory.
     *
     * @param dir the monitored directory for which to create the menu item
     * @return the JMenuItem for restoring the note
     */
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
                // User cancelled
                if (input == null || input.isBlank())
                    return;
                // Handle reset baseline
                listener.onResetBaseline(dir, Long.parseLong(input));
            } catch (NumberFormatException ex) {
                listener.showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR_INVALID);
            }
        });

        return resetBaselineItem;
    }
}

