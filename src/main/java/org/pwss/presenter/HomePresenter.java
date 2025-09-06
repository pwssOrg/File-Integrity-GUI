package org.pwss.presenter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.model.service.ScanService;
import org.pwss.model.service.ScanSummaryService;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.model.table.ScanTableModel;
import org.pwss.view.screen.HomeScreen;

public class HomePresenter extends BasePresenter<HomeScreen> {
    private final ScanService scanService;
    private final MonitoredDirectoryService monitoredDirectoryService;
    private final ScanSummaryService scanSummaryService;

    private List<MonitoredDirectory> allMonitoredDirectories;
    private List<Scan> recentScans;

    public HomePresenter(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
        this.scanSummaryService = new ScanSummaryService();
        fetchData();
    }

    @Override
    protected void initListeners() {
        getScreen().getScanButton().addActionListener(e -> handleScanButtonClick());
        getScreen().getQuickScanButton().addActionListener(e -> handleScanButtonClick());
    }

    private void fetchData() {
        try {
            allMonitoredDirectories = monitoredDirectoryService.getAllDirectories();
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            recentScans = scanService.getMostRecentScans();
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        updateUiState();
    }

    private void updateUiState() {
        // Update UI components based on the current state
        try {
            boolean isScanRunning = scanService.scanRunning();
            getScreen().getScanButton().setText(isScanRunning ? "Stop Scan" : "Start Scan");
            getScreen().getQuickScanButton().setText(isScanRunning ? "Stop Scan" : "Quick Scan");

            ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans);
            getScreen().getRecentScanTable().setModel(mostRecentScansListModel);

            MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(allMonitoredDirectories);
            getScreen().getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
        } catch (ScanStatusException | ExecutionException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error updating UI state: " + e.getMessage()));
        }
    }

    private void handleScanButtonClick() {
        // Toggle scan state
        try {
            if (scanService.scanRunning()) {
                performStopScan();
            } else {
                performStartScan();
            }
        } catch (ScanStatusException | ExecutionException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error starting scan: " + e.getMessage()));
        }
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
