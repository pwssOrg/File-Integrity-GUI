package org.pwss.controller;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.utils.OSUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.NewDirectoryScreen;

/**
 * Controller class that handles operations related to the New Directory screen.
 */
public class NewDirectoryController extends BaseController<NewDirectoryScreen> {

    /**
     * Service responsible for managing monitored directories.
     */
    private final MonitoredDirectoryService monitoredDirectoryService;

    /**
     * The currently selected directory path. This can be null if no path is selected.
     */
    private String selectedPath;

    /**
     * Constructs a NewDirectoryController with the given screen.
     *
     * @param screen The screen instance that this controller will manage
     */
    public NewDirectoryController(NewDirectoryScreen screen) {
        super(screen);
        this.monitoredDirectoryService = new MonitoredDirectoryService();
    }

    @Override
    public void onShow() {
        super.onShow();
        // Reset selected path when the screen is shown
        selectedPath = null;
        refreshView();
    }

    @Override
    void initListeners() {
        screen.getSelectPathButton().addActionListener(e -> openFolderPicker());
        screen.getCancelButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.HOME));
        screen.getCreateButton().addActionListener(e -> createNewDirectory());
    }

    @Override
    void refreshView() {
        screen.getPathLabel().setText(selectedPath != null ? selectedPath : StringConstants.NEW_DIR_NO_PATH_SELECTED);
        screen.getCreateButton().setEnabled(selectedPath != null && !selectedPath.isEmpty());

        // Additional check for Unix-based systems to disable creation for /dev and /proc paths
        if (OSUtils.isUnix() && selectedPath != null) {
            if (selectedPath.startsWith("/dev") || selectedPath.startsWith("/proc")) {
                screen.getCreateButton().setEnabled(false);
                screen.showError("Cannot monitor directories under /dev or /proc on Unix-based systems.");
            }
        }
    }

    /**
     * Opens a folder picker dialog to allow the user to select a directory.
     * Updates the selected path and refreshes the view accordingly.
     */
    private void openFolderPicker() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = folderChooser.showOpenDialog(getScreen().getRootPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedPath = folderChooser.getSelectedFile().getAbsolutePath();
            refreshView();
        }
    }

    /**
     * Creates a new monitored directory with the selected path and options.
     * Displays success or error messages based on the operation result.
     * Navigates back to the home screen upon successful creation.
     */
    private void createNewDirectory() {
        boolean includeSubdirectories = screen.getIncludeSubdirectoriesCheckBox().isSelected();
        boolean makeActive = screen.getMakeDirectoryActiveCheckBox().isSelected();
        if (selectedPath != null && !selectedPath.isEmpty()) {
            try {
                monitoredDirectoryService.createNewMonitoredDirectory(selectedPath, includeSubdirectories, makeActive);
                JOptionPane.showMessageDialog(screen.getRootPanel(), StringConstants.NEW_DIR_SUCCESS_TEXT,
                        StringConstants.GENERIC_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                NavigationEvents.navigateTo(Screen.HOME);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(screen.getRootPanel(),
                        StringConstants.NEW_DIR_ERROR_PREFIX + e.getMessage(), StringConstants.GENERIC_ERROR,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
