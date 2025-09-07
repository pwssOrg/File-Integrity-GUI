package org.pwss.presenter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StartScanByIdException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.model.service.ScanService;
import org.pwss.model.service.ScanSummaryService;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.model.table.ScanTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.view.screen.HomeScreen;

public class HomePresenter extends BasePresenter<HomeScreen> {
    private final ScanService scanService;
    private final MonitoredDirectoryService monitoredDirectoryService;
    private final ScanSummaryService scanSummaryService;

    private List<MonitoredDirectory> allMonitoredDirectories;
    private List<Scan> recentScans;
    private List<ScanSummary> recentScanSummaries;

    private boolean scanRunning;
    private Timer scanStatusTimer;

    public HomePresenter(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
        this.scanSummaryService = new ScanSummaryService();
        fetchDataAndRefreshView();
    }

    void fetchDataAndRefreshView() {
        // Fetch all monitored directories for display in the monitored directories table
        try {
            allMonitoredDirectories = monitoredDirectoryService.getAllDirectories();
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Fetch recent scans for display in the scan table
        try {
            recentScans = scanService.getMostRecentScans();
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        refreshView();
    }

    @Override
    public void reloadData() {
        fetchDataAndRefreshView();
    }

    @Override
    protected void initListeners() {
        getScreen().getAddNewDirectoryButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.NEW_DIRECTORY));
        getScreen().getScanButton().addActionListener(e -> handleScanButtonClick(false));
        getScreen().getQuickScanButton().addActionListener(e -> handleScanButtonClick(false));
        getScreen().getScanSingleButton().addActionListener(e -> handleScanButtonClick(true));
    }

    @Override
    protected void refreshView() {
        // Update UI components based on the current state
        try {
            boolean isScanRunning = scanService.scanRunning();
            getScreen().getScanButton().setText(isScanRunning ? "Stop Scan" : "Full Scan");
            getScreen().getScanSingleButton().setText(isScanRunning ? "Stop Scan" : "Scan Selected");
            getScreen().getQuickScanButton().setText(isScanRunning ? "Stop Scan" : "Quick Scan");
            getScreen().getScanProgressContainer().setVisible(scanRunning);

            ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans);
            getScreen().getRecentScanTable().setModel(mostRecentScansListModel);

            MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(allMonitoredDirectories);
            getScreen().getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
        } catch (ScanStatusException | ExecutionException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error updating UI state: " + e.getMessage()));
        }
    }

    private void handleScanButtonClick(boolean singleDirectory) {
        // Toggle scan state
        try {
            if (scanService.scanRunning()) {
                performStopScan();
            } else {
                performStartScan(singleDirectory);
            }
        } catch (ScanStatusException | ExecutionException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error starting scan: " + e.getMessage()));
        }
    }

    /**
     * Initiates a scan operation.
     *
     * @param singleDirectory if true, scans only the selected directory; if false, scans all directories.
     */
    private void performStartScan(boolean singleDirectory) {
        try {
            boolean startScanSuccess;
            JTable table = getScreen().getMonitoredDirectoriesTable();
            MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) table.getModel();
            if (singleDirectory){
                int viewRow = table.getSelectedRow();
                if (viewRow == -1) {
                    startScanSuccess = false;
                } else {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    MonitoredDirectory dir = model.getDirectoryAt(modelRow);
                    startScanSuccess = scanService.startScanById(dir.id());
                }
            } else {
                startScanSuccess = scanService.startScan();
            }
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    getScreen().showSuccess("Scan started successfully!");
                    startScanStatusPolling();
                } else {
                    getScreen().showError("Failed to start scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException | StartScanByIdException | JsonProcessingException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error starting scan: " + e.getMessage()));
        }
    }

    /**
     * Stops an ongoing scan operation.
     */
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

    private void startScanStatusPolling() {
        if (scanStatusTimer != null && scanStatusTimer.isRunning()) {
            return; // already polling
        }

        scanStatusTimer = new Timer(1000, e -> {
            try {
                boolean running = scanService.scanRunning();
                if (running != scanRunning) {
                    scanRunning = running;
                    refreshView(); // update UI if state changed
                }
                if (!running) {
                    scanStatusTimer.stop(); // stop polling when scan ends
                    fetchDataAndRefreshView(); // refresh data when scan ends to get latest scans
                }
            } catch (ScanStatusException | ExecutionException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> screen.showError("Error checking scan status: " + ex.getMessage()));
            }
        });
        scanStatusTimer.start();
    }
}
