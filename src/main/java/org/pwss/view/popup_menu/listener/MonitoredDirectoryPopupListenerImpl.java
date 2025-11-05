package org.pwss.view.popup_menu.listener;

import java.awt.Component;
import org.pwss.controller.HomeController;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.request.notes.RestoreNoteType;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.service.NoteService;
import org.pwss.utils.StringConstants;
import org.slf4j.LoggerFactory;

/**
 * Implementation of MonitoredDirectoryPopupListener that handles interactions
 * related to monitored directories.
 */
public class MonitoredDirectoryPopupListenerImpl implements MonitoredDirectoryPopupListener {
    /**
     * Logger instance for logging messages within this implementation.
     */
    private final org.slf4j.Logger log;
    /**
     * Home controller instance responsible for managing the home screen operations.
     */
    private final HomeController controller;
    /**
     * Service responsible for managing monitored directories.
     */
    private final MonitoredDirectoryService directoryService;
    /**
     * Service responsible for managing notes associated with monitored directories.
     */
    private final NoteService noteService;

    /**
     * Constructs a new instance of MonitoredDirectoryPopupListenerImpl.
     *
     * @param controller       The HomeController to manage home screen operations
     * @param directoryService The service to manage monitored directories
     * @param noteService      The service to manage notes associated with monitored
     *                         directories
     */
    public MonitoredDirectoryPopupListenerImpl(HomeController controller, MonitoredDirectoryService directoryService,
            NoteService noteService) {
        this.log = LoggerFactory.getLogger(MonitoredDirectoryPopupListenerImpl.class);
        this.controller = controller;
        this.directoryService = directoryService;
        this.noteService = noteService;
    }

    @Override
    public void onStartScan() {
        controller.performStartScan(true);
    }

    @Override
    public void onResetBaseline(MonitoredDirectory dir, long endpointCode) {
        try {
            if (directoryService.newMonitoredDirectoryBaseline(dir.id(), endpointCode)) {
                showSuccess(StringConstants.MON_DIR_POPUP_RESET_BASELINE_SUCCESS);
                controller.reloadData();
            } else {
                showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR);
            }
        } catch (Exception e) {
            log.error("Error resetting baseline for directory {}: {}", dir.path(), e.getMessage());
            showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR_PREFIX + e.getMessage());
        }
    }

    @Override
    public void onToggleActiveStatus(MonitoredDirectory dir) {
        try {
            if (directoryService.toggleActive(dir)) {
                showSuccess(StringConstants.MON_DIR_TOGGLE_ACTIVE_SUCCESS);
                controller.reloadData();
            } else {
                showError(StringConstants.MON_DIR_TOGGLE_ACTIVE_ERROR);
            }
        } catch (Exception e) {
            log.error("Error toggling active status for directory {}: {}", dir.path(), e.getMessage());
            showError(e.getMessage());
        }
    }

    @Override
    public void onToggleIncludeSubdirectories(MonitoredDirectory dir) {
        try {
            if (directoryService.toggleIncludeSubDirectories(dir)) {
                showSuccess(StringConstants.MON_DIR_TOGGLE_INCLUDE_SUBDIR_SUCCESS);
                controller.reloadData();
            } else {
                showError(StringConstants.MON_DIR_TOGGLE_INCLUDE_SUBDIR_ERROR);
            }
        } catch (Exception e) {
            log.error("Error toggling include subdirectories for directory {}: {}", dir.path(), e.getMessage());
            showError(e.getMessage());
        }
    }

    @Override
    public void onUpdateNote(MonitoredDirectory dir, String newNotes) {
        try {
            if (noteService.updateNotes(dir.notes().id(), newNotes)) {
                showSuccess(StringConstants.MON_DIR_POPUP_UPDATE_NOTE_SUCCESS);
                controller.reloadData();
            } else {
                showError(StringConstants.MON_DIR_POPUP_UPDATE_NOTE_ERROR);
            }
        } catch (Exception e) {
            log.error("Error updating notes for directory {}: {}", dir.path(), e.getMessage());
            showError(e.getMessage());
        }
    }

    @Override
    public void onRestoreNote(MonitoredDirectory dir, RestoreNoteType restoreType) {
        try {
            if (noteService.restoreNotes(dir.notes().id(), restoreType)) {
                showSuccess(StringConstants.MON_DIR_POPUP_RESTORE_NOTE_SUCCESS);
                controller.reloadData();
            } else {
                showError(StringConstants.MON_DIR_POPUP_RESTORE_NOTE_ERROR);
            }
        } catch (Exception e) {
            log.error("Error restoring notes for directory {}: {}", dir.path(), e.getMessage());
            showError(e.getMessage());
        }
    }

    @Override
    public void showSuccess(String message) {
        controller.getScreen().showSuccess(message);
    }

    @Override
    public void showError(String message) {
        controller.getScreen().showError(message);
    }

    @Override
    public Component getParentComponent() {
        return controller.getScreen().getRootPanel();
    }
}
