package org.pwss.presenter;

import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.view.screen.NewDirectoryScreen;

import javax.swing.*;

public class NewDirectoryPresenter extends BasePresenter<NewDirectoryScreen> {
    private final MonitoredDirectoryService monitoredDirectoryService;
    private String selectedPath = null;

    public NewDirectoryPresenter(NewDirectoryScreen screen) {
        super(screen);
        this.monitoredDirectoryService = new MonitoredDirectoryService();
    }

    @Override
    public void onShow() {
        refreshView();
    }

    @Override
    void initListeners() {
        getScreen().getSelectPathButton().addActionListener(e -> openFolderPicker());
        getScreen().getCancelButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.HOME, null));
        getScreen().getCreateButton().addActionListener(e -> createNewDirectory());
    }

    @Override
    void refreshView() {
        getScreen().getPathLabel().setText(selectedPath != null ? selectedPath : "No folder selected");
        getScreen().getCreateButton().setEnabled(selectedPath != null && !selectedPath.isEmpty());
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
        boolean includeSubdirectories = getScreen().getIncludeSubdirectoriesCheckBox().isSelected();
        boolean makeActive = getScreen().getMakeDirectoryActiveCheckBox().isSelected();
        if (selectedPath != null && !selectedPath.isEmpty()) {
            try {
                monitoredDirectoryService.createNewMonitoredDirectory(selectedPath, includeSubdirectories, makeActive);
                JOptionPane.showMessageDialog(getScreen().getRootPanel(), "Directory added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                NavigationEvents.navigateTo(Screen.HOME, null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(getScreen().getRootPanel(), "Failed to add directory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
