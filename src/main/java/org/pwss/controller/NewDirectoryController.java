package org.pwss.controller;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.util.OSUtil;
import org.pwss.util.StringConstants;
import org.pwss.view.screen.NewDirectoryScreen;

/**
 * Controller for adding a monitored directory.
 * <p>
 * The form opens as a modal dialog on top of the current window (see
 * {@link #showDialog(Component)}), so the main window keeps its size, page
 * and state while a directory is added.
 */
public class NewDirectoryController extends BaseController<NewDirectoryScreen> {

    /**
     * Service used to create the monitored directory.
     */
    private final MonitoredDirectoryService monitoredDirectoryService;

    /**
     * The directory picked by the user, or null if none was picked yet.
     */
    private String selectedPath;

    /**
     * The open dialog, or null while the form is not shown.
     */
    private JDialog dialog;

    /**
     * Whether a directory was added while the dialog was open.
     */
    private boolean created;

    /**
     * Constructs a NewDirectoryController with the given screen.
     *
     * @param screen the form to control
     */
    public NewDirectoryController(NewDirectoryScreen screen) {
        super(screen);
        this.monitoredDirectoryService = new MonitoredDirectoryService();
    }

    /**
     * Shows the form as a modal dialog over the window holding the given
     * component and waits until it is closed.
     *
     * @param parent a component of the window to open the dialog over
     * @return true if a directory was added
     */
    public boolean showDialog(Component parent) {
        Window owner = parent instanceof Window window ? window : SwingUtilities.getWindowAncestor(parent);
        dialog = new JDialog(owner, "Add a monitored directory", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(screen.getRootPanel());
        dialog.getRootPane().setDefaultButton(screen.getCreateButton());
        dialog.getRootPane().registerKeyboardAction(e -> closeDialog(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.setResizable(false);

        created = false;
        onShow();

        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true); // blocks until the dialog is closed

        dialog = null;
        return created;
    }

    @Override
    public void onShow() {
        super.onShow();
        // Start every time with an empty form
        selectedPath = null;
        refreshView();
    }

    @Override
    void initListeners() {
        screen.getSelectPathButton().addActionListener(e -> openFolderPicker());
        screen.getCancelButton().addActionListener(e -> closeDialog());
        screen.getCreateButton().addActionListener(e -> createNewDirectory());

        // The path field itself opens the picker too
        screen.getPathLabel().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        screen.getPathLabel().setToolTipText("Click to choose a directory");
        screen.getPathLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openFolderPicker();
            }
        });
    }

    @Override
    void refreshView() {
        screen.getPathLabel().setText(selectedPath != null ? selectedPath : StringConstants.NEW_DIR_NO_PATH_SELECTED);
        screen.getCreateButton().setEnabled(selectedPath != null && !selectedPath.isEmpty());

        // Additional check for Unix-based systems to disable creation for /dev and /proc paths
        if (OSUtil.isUnix() && selectedPath != null) {
            if (selectedPath.startsWith("/dev") || selectedPath.startsWith("/proc")) {
                screen.getCreateButton().setEnabled(false);
                screen.showError("Cannot monitor directories under /dev or /proc on Unix-based systems.");
            }
        }
        if (dialog != null) {
            dialog.pack();
        }
    }

    private void closeDialog() {
        if (dialog != null) {
            dialog.dispose();
        }
    }

    /**
     * Opens a folder picker, starting at the previously picked directory.
     */
    private void openFolderPicker() {
        JFileChooser folderChooser = new JFileChooser(selectedPath != null ? new File(selectedPath) : null);
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setDialogTitle("Choose a directory to monitor");
        int returnValue = folderChooser.showOpenDialog(screen.getRootPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedPath = folderChooser.getSelectedFile().getAbsolutePath();
            refreshView();
        }
    }

    /**
     * Creates the monitored directory. On success the dialog closes; on failure
     * it stays open with the form intact, so the user can try again.
     */
    private void createNewDirectory() {
        boolean includeSubdirectories = screen.getIncludeSubdirectoriesCheckBox().isSelected();
        boolean makeActive = screen.getMakeDirectoryActiveCheckBox().isSelected();
        if (selectedPath != null && !selectedPath.isEmpty()) {
            try {
                monitoredDirectoryService.createNewMonitoredDirectory(selectedPath, includeSubdirectories, makeActive);
                created = true;
                closeDialog();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(screen.getRootPanel(),
                        StringConstants.NEW_DIR_ERROR_PREFIX + e.getMessage(), StringConstants.GENERIC_ERROR,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
