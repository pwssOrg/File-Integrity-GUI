package org.pwss.presenter;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.service.ScanService;
import org.pwss.view.screen.HomeScreen;

public class HomePresenter extends BasePresenter<HomeScreen> {
    private final ScanService scanService;

    public HomePresenter(HomeScreen view, ScanService scanService) {
        super(view);
        this.scanService = scanService;
    }

    @Override
    protected void initListeners() {
        getScreen().getStartButton().addActionListener(e -> performStartScan());
        getScreen().getStopButton().addActionListener(e -> performStopScan());
    }

    private void performStartScan() {
        try {
            boolean startScanSuccess = scanService.startScan();
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    getScreen().showSuccess("Scan started successfully!");
                } else {
                    getScreen().showError("Failed to start scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error starting scan: " + e.getMessage()));
        }
    }

    private void performStopScan() {
        try {
            boolean stopScanSuccess = scanService.stopScan();
            SwingUtilities.invokeLater(() -> {
                if (stopScanSuccess) {
                    getScreen().showSuccess("Scan stopped successfully!");
                } else {
                    getScreen().showError("Failed to stop scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StopScanException e) {
            SwingUtilities.invokeLater(() -> getScreen().showError("Error stopping scan: " + e.getMessage()));
        }
    }
}
