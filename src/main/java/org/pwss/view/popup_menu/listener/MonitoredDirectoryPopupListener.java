package org.pwss.view.popup_menu.listener;

import java.awt.Component;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.request.notes.RestoreNoteType;

/**
 * Listener interface for handling actions from the monitored directory popup menu.
 * Implement this interface to define custom behavior for each menu action.
 */
public interface MonitoredDirectoryPopupListener {
    /**
     * Triggered when a scan operation is started.
     */
    void onStartScan();

    /**
     * Triggered when the baseline for a monitored directory is reset.
     *
     * @param dir          The monitored directory for which the baseline is being reset.
     * @param endpointCode The endpoint code to confirm the reset action.
     */
    void onResetBaseline(MonitoredDirectory dir, long endpointCode);

    /**
     * Triggered when a monitored directory is edited.
     *
     * @param dir The monitored directory to be edited.
     */
    void onEditDirectory(MonitoredDirectory dir);

    /**
     * Triggered when the notes for a monitored directory are updated.
     *
     * @param dir      The monitored directory whose notes are being updated.
     * @param newNotes The new notes to be set.
     */
    void onUpdateNote(MonitoredDirectory dir, String newNotes);

    /**
     * Triggered when the notes for a monitored directory are restored to a previous state.
     *
     * @param dir         The monitored directory whose notes are being restored.
     * @param restoreType The type of restoration to be performed. (PREV_NOTE or PREV_PREV_NOTE)
     */
    void onRestoreNote(MonitoredDirectory dir, RestoreNoteType restoreType);

    /**
     * Displays a success message to the user.
     *
     * @param message The success message to be displayed.
     */
    void showSuccess(String message);

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    void showError(String message);

    /**
     * Retrieves the parent component associated with this listener.
     *
     * @return The parent component.
     */
    Component getParentComponent();
}
