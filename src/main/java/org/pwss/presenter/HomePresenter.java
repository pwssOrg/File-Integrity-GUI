package org.pwss.presenter;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.service.ScanService;
import org.pwss.view.screen.HomeView;

public class HomePresenter extends BasePresenter<HomeView> {
    private final ScanService scanService;

    public HomePresenter(HomeView view, ScanService scanService) {
        super(view);
        this.scanService = scanService;
    }

    @Override
    protected void initListeners() {
        view.getStartButton().addActionListener(e -> performStartScan());
        view.getStopButton().addActionListener(e -> performStopScan());
    }

    private void performStartScan() {
        try {
            boolean startScanSuccess = scanService.startScan();
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    view.showSuccess("Scan started successfully!");
                } else {
                    view.showError("Failed to start scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException e) {
            SwingUtilities.invokeLater(() -> view.showError("Error starting scan: " + e.getMessage()));
        }
    }

    private void performStopScan() {
        try {
            boolean stopScanSuccess = scanService.stopScan();
            SwingUtilities.invokeLater(() -> {
                if (stopScanSuccess) {
                    view.showSuccess("Scan stopped successfully!");
                } else {
                    view.showError("Failed to stop scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StopScanException e) {
            SwingUtilities.invokeLater(() -> view.showError("Error stopping scan: " + e.getMessage()));
        }
    }
}
