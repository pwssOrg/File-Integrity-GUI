package org.pwss.presenter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.*;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.model.service.ScanService;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.model.table.ScanTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.presenter.util.NavigationContext;
import org.pwss.view.screen.HomeScreen;

public class HomePresenter extends BasePresenter<HomeScreen> {
    private final ScanService scanService;
    private final MonitoredDirectoryService monitoredDirectoryService;

    private List<MonitoredDirectory> allMonitoredDirectories;
    private List<Scan> recentScans;

    private boolean scanRunning;
    private Timer scanStatusTimer;

    public HomePresenter(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
    }

    @Override
    public void onShow() {
        fetchDataAndRefreshView();
    }

    /**
     * Fetches data from services and refreshes the view.
     * This method retrieves all monitored directories and recent scans,
     * then updates the UI accordingly.
     */
    void fetchDataAndRefreshView() {
        try {
            // Fetch all monitored directories for display in the monitored directories table
            allMonitoredDirectories = monitoredDirectoryService.getAllDirectories();
            // Fetch recent scans for display in the scan table
            recentScans = scanService.getMostRecentScansAll();
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException |
                 JsonProcessingException | GetAllMostRecentScansException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error getting data: " + e.getMessage()));
        }
        // Finally, refresh the view to reflect the updated data
        refreshView();
    }

    @Override
    public void reloadData() {
        fetchDataAndRefreshView();
    }

    @Override
    protected void initListeners() {
        screen.getAddNewDirectoryButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.NEW_DIRECTORY, null));
        screen.getScanButton().addActionListener(e -> handleScanButtonClick(false));
        screen.getQuickScanButton().addActionListener(e -> handleScanButtonClick(false));
        screen.getScanSingleButton().addActionListener(e -> handleScanButtonClick(true));
        // Double-click listener for recent scans table to view scan details
        screen.getRecentScanTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && screen.getRecentScanTable().getSelectedRow() != -1) {
                    int viewRow = screen.getRecentScanTable().getSelectedRow();
                    int modelRow = screen.getRecentScanTable().convertRowIndexToModel(viewRow);

                    ScanTableModel model = (ScanTableModel) screen.getRecentScanTable().getModel();
                    Scan scan = model.getScanAt(modelRow);

                    if (scan != null) {
                        NavigationContext context = new NavigationContext();
                        context.put("scanId", scan.id());
                        NavigationEvents.navigateTo(Screen.SCAN_SUMMARY, context);
                    }
                }
            }
        });
    }

    @Override
    protected void refreshView() {
        // Update UI components based on the current state
        try {
            boolean isScanRunning = scanService.scanRunning();
            screen.getScanButton().setText(isScanRunning ? "Stop Scan" : "Full Scan");
            screen.getScanSingleButton().setText(isScanRunning ? "Stop Scan" : "Scan Selected");
            screen.getQuickScanButton().setText(isScanRunning ? "Stop Scan" : "Quick Scan");
            screen.getScanProgressContainer().setVisible(scanRunning);

            ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans);
            screen.getRecentScanTable().setModel(mostRecentScansListModel);

            MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(allMonitoredDirectories);
            screen.getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
        } catch (ScanStatusException | ExecutionException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error updating UI state: " + e.getMessage()));
        }
    }

    /**
     * Handles the logic when the scan button is clicked.
     *
     * @param singleDirectory if true, scans only the selected directory; if false, scans all directories.
     */
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
            JTable table = screen.getMonitoredDirectoriesTable();
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
                    screen.showSuccess("Scan started successfully!");
                    startScanStatusPolling(singleDirectory);
                } else {
                    screen.showError("Failed to start scan.");
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
                    screen.showSuccess("Scan stopped successfully!");
                } else {
                    screen.showError("Failed to stop scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StopScanException e) {
            SwingUtilities.invokeLater(() -> screen.showError("Error stopping scan: " + e.getMessage()));
        }
    }

    /**
     * Starts polling the scan status at regular intervals.
     *
     * @param singleDirectory if true, indicates that a single directory scan was initiated.
     */
    private void startScanStatusPolling(boolean singleDirectory) {
        if (scanStatusTimer != null && scanStatusTimer.isRunning()) {
            return; // already polling
        }

        scanStatusTimer = new Timer(1500, e -> {
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
