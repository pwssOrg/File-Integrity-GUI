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
import org.pwss.model.service.response.LiveFeedResponse;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.model.table.ScanTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.presenter.util.NavigationContext;
import org.pwss.utils.LiveFeedUtils;
import org.pwss.view.screen.HomeScreen;

public class HomePresenter extends BasePresenter<HomeScreen> {
    private final ScanService scanService;
    private final MonitoredDirectoryService monitoredDirectoryService;

    private List<MonitoredDirectory> allMonitoredDirectories;
    private List<Scan> recentScans;

    private boolean scanRunning;
    private long totalDiffCount = 0;
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
            // Check if a scan is currently running
            boolean scanCurrentlyRunning = scanService.scanRunning();
            // If a scan has started since the last check, initiate polling
            if (scanCurrentlyRunning && !scanRunning) {
                scanRunning = true;
                startPollingScanLiveFeed(false);
            }
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException |
                 JsonProcessingException | GetAllMostRecentScansException | ScanStatusException e) {
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
        screen.getMonitoredDirectoriesTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && screen.getMonitoredDirectoriesTable().getSelectedRow() != -1) {
                    int viewRow = screen.getMonitoredDirectoriesTable().getSelectedRow();
                    int modelRow = screen.getMonitoredDirectoriesTable().convertRowIndexToModel(viewRow);

                    MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) screen.getMonitoredDirectoriesTable().getModel();
                    MonitoredDirectory dir = model.getDirectoryAt(modelRow);

                    if (dir != null) {
                        NavigationContext context = new NavigationContext();
                        handleScanButtonClick(true);
                    }
                }
            }
        });
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
        screen.getScanButton().setText(scanRunning ? "Stop Scan" : "Full Scan");
        screen.getQuickScanButton().setText(scanRunning ? "Stop Scan" : "Quick Scan");

        // Scan running views
        screen.getScanProgressContainer().setVisible(scanRunning);
        screen.getLiveFeedContainer().setVisible(scanRunning);
        screen.getLiveFeedTitle().setVisible(scanRunning);
        screen.getLiveFeedDiffCount().setVisible(scanRunning);

        ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans);
        screen.getRecentScanTable().setModel(mostRecentScansListModel);

        MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(allMonitoredDirectories);
        screen.getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
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
            if (singleDirectory) {
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
                    startPollingScanLiveFeed(singleDirectory);
                } else {
                    screen.showError("Failed to start scan.");
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException | StartScanByIdException |
                 JsonProcessingException e) {
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

    private void onFinishScan(boolean completed, boolean singleDirectory) {
        // Refresh data to display the latest scan results
        fetchDataAndRefreshView();
        // Notify the user about the scan results
        if (totalDiffCount > 0 && completed) {
            int choice = screen.showOptionDialog(JOptionPane.WARNING_MESSAGE, "Scan completed with differences found.\nView details?\nYou can always view results later in the recent scans table.", new String[]{"Yes", "No"}, "Yes");
            if (choice == 0) {
                if (singleDirectory) {
                    // If single directory scan, navigate to the scan summary of the most recent scan.
                    try {
                        Scan latestScan = scanService.getMostRecentScans(1).getFirst();
                        NavigationContext context = new NavigationContext();
                        context.put("scanId", latestScan.id());
                        NavigationEvents.navigateTo(Screen.SCAN_SUMMARY, context);
                    } catch (GetMostRecentScansException | ExecutionException | InterruptedException | JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // If full scan, set current tab to home screen where scans are listed so user can select.
                    screen.getTabbedPane().setSelectedIndex(0);
                }
            }
        } else if (completed) {
            // Scan completed with no differences
            screen.showSuccess("Scan completed with no differences found.");
        } else {
            // Scan did not complete successfully
            screen.showError("Scan did not complete successfully.");
        }
        // Reset diff count for the next scan
        totalDiffCount = 0;
        // Clear the live feed text area in preparation for the next scan
        screen.getLiveFeedText().setText("");
    }

    /**
     * Starts polling the live feed for scan updates.
     * This method sets up a timer to periodically fetch live feed updates
     * and update the UI accordingly.
     * @param singleDirectory if true, indicates that the scan is for a single directory; otherwise, for all directories.
     */
    private void startPollingScanLiveFeed(boolean singleDirectory) {
        if (scanStatusTimer != null && scanStatusTimer.isRunning()) {
            return; // Polling is already active
        }

        scanStatusTimer = new Timer(1000, e -> {
            try {
                // Retrieve live feed updates
                LiveFeedResponse liveFeed = scanService.getLiveFeed();

                String currentLiveFeedText = screen.getLiveFeedText().getText();
                String newEntry = LiveFeedUtils.formatLiveFeedEntry(liveFeed.livefeed());
                String updatedLiveFeedText = currentLiveFeedText + newEntry;
                screen.getLiveFeedText().setText(updatedLiveFeedText);

                // Update the total difference count based on new warnings
                totalDiffCount += LiveFeedUtils.countWarnings(liveFeed.livefeed());
                screen.getLiveFeedDiffCount().setText("Diffs: " + totalDiffCount);

                // Update scanRunning state and refresh the UI if necessary
                if (liveFeed.isScanRunning() != scanRunning) {
                    scanRunning = liveFeed.isScanRunning();
                    refreshView();
                }
                if (!liveFeed.isScanRunning()) {
                    scanStatusTimer.stop(); // Terminate polling when the scan completes
                    onFinishScan(true, singleDirectory);
                }
            } catch (LiveFeedException | ExecutionException | InterruptedException | JsonProcessingException ex) {
                SwingUtilities.invokeLater(() -> screen.showError("An error occurred while retrieving the live feed: " + ex.getMessage()));
                scanStatusTimer.stop(); // Stop polling on error
                onFinishScan(false, singleDirectory);
            }
        });
        scanStatusTimer.start();
    }
}
