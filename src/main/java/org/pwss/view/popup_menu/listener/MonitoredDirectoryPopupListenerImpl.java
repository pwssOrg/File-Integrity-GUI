package org.pwss.view.popup_menu.listener;

import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.controller.HomeController;
import org.pwss.utils.StringConstants;

import java.awt.Component;

public class MonitoredDirectoryPopupListenerImpl implements MonitoredDirectoryPopupListener{
    private final HomeController controller;
    private final MonitoredDirectoryService directoryService;

    public MonitoredDirectoryPopupListenerImpl(HomeController controller, MonitoredDirectoryService directoryService) {
        this.controller = controller;
        this.directoryService = directoryService;
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
        } catch (Exception  e) {
            showError(StringConstants.MON_DIR_POPUP_RESET_BASELINE_ERROR_PREFIX + e.getMessage());
        }
    }

    @Override
    public void onEditDirectory(MonitoredDirectory dir) {

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
