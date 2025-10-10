package org.pwss.view.popup_menu.listener;

import java.awt.Component;
import org.pwss.controller.HomeController;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.request.notes.RestoreNoteType;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.service.NoteService;
import org.pwss.utils.StringConstants;
import org.slf4j.LoggerFactory;

public class MonitoredDirectoryPopupListenerImpl implements MonitoredDirectoryPopupListener {
    private final org.slf4j.Logger log;

    private final HomeController controller;
    private final MonitoredDirectoryService directoryService;
    private final NoteService noteService;

    public MonitoredDirectoryPopupListenerImpl(HomeController controller, MonitoredDirectoryService directoryService, NoteService noteService) {
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
    public void onEditDirectory(MonitoredDirectory dir) {
        // TODO: Implement edit directory functionality
        
        // We need to be able to update and save notes. Maybe here? :) / Pwgit-Create 
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
