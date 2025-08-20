package org.pwss.presenter;

import org.pwss.model.service.ScanService;
import org.pwss.view.screen.ScanView;

import javax.swing.*;

public class ScanPresenter extends BasePresenter<ScanView> {
    private final ScanService scanService;

    public ScanPresenter(ScanView view, ScanService scanService) {
        super(view);
        this.scanService = scanService;
    }

    @Override
    protected void initListeners() {
        view.getStartButton().addActionListener(e -> performStartScan());
        view.getStopButton().addActionListener(e -> performStopScan());
    }

    private void performStartScan() {
        scanService.startScan().thenAccept(success -> SwingUtilities.invokeLater(() -> {
            if (success) {
                view.showSuccess("Scan started successfully!");
            } else {
                view.showError("Failed to start scan.");
            }
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> view.showError("Error: " + ex.getMessage()));
            return null;
        });
    }

    private void performStopScan() {
        scanService.stopScan().thenAccept(success -> SwingUtilities.invokeLater(() -> {
            if (success) {
                view.showSuccess("Scan stopped successfully!");
            } else {
                view.showError("Failed to stop scan.");
            }
        })).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> view.showError("Error: " + ex.getMessage()));
            return null;
        });
    }
}
