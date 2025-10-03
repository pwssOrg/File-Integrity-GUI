package org.pwss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.pwss.controller.util.NavigationContext;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.*;
import org.pwss.exception.scan_summary.GetSearchFilesException;
import org.pwss.exception.scan_summary.GetSummaryForFileException;
import org.pwss.model.entity.*;
import org.pwss.model.service.MonitoredDirectoryService;
import org.pwss.model.service.ScanService;
import org.pwss.model.service.ScanSummaryService;
import org.pwss.model.service.response.LiveFeedResponse;
import org.pwss.model.table.*;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.utils.LiveFeedUtils;
import org.pwss.utils.ReportUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.popup_menu.MonitoredDirectoryPopupFactory;
import org.pwss.view.popup_menu.listener.MonitoredDirectoryPopupListenerImpl;
import org.pwss.view.screen.HomeScreen;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class HomeController extends BaseController<HomeScreen> {
    private final ScanService scanService;
    private final MonitoredDirectoryService monitoredDirectoryService;
    private final ScanSummaryService scanSummaryService;
    private final MonitoredDirectoryPopupFactory monitoredDirectoryPopupFactory;

    private List<MonitoredDirectory> allMonitoredDirectories;
    private List<Scan> recentScans;
    private List<Diff> recentDiffs;
    private List<File> fileResults;
    private List<ScanSummary> fileSummaries;

    private boolean scanRunning;
    private long totalDiffCount = 0;
    private Timer scanStatusTimer;

    public HomeController(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
        scanSummaryService = new ScanSummaryService();
        this.monitoredDirectoryPopupFactory = new MonitoredDirectoryPopupFactory(new MonitoredDirectoryPopupListenerImpl(this, monitoredDirectoryService));
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
            if (recentScans.isEmpty()) {
                recentDiffs = List.of();
            } else {
                // Fetch diffs for the most recent scan to show in the diffs table
                recentDiffs = recentScans.stream()
                        .flatMap(scan -> safeGetDiffs(scan.id()).stream())
                        .toList();
            }
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

    /**
     * Safely retrieves diffs for a given scan ID.
     * This method handles exceptions and returns an empty list in case of errors.
     *
     * @param scanId the ID of the scan for which to retrieve diffs
     * @return a list of diffs associated with the scan, or an empty list if an error occurs
     */
    private List<Diff> safeGetDiffs(long scanId) {
        try {
            return scanService.getDiffs(scanId, 1000, null, false);
        } catch (GetScanDiffsException | ExecutionException | InterruptedException | JsonProcessingException e) {
            return List.of();
        }
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
            public void mousePressed(MouseEvent e) {
                showPopupIfTriggered(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupIfTriggered(e);
            }

            private void showPopupIfTriggered(MouseEvent e) {
                if (e.isPopupTrigger() && screen.getMonitoredDirectoriesTable().getSelectedRow() != -1) {
                    int viewRow = screen.getMonitoredDirectoriesTable().getSelectedRow();
                    int modelRow = screen.getMonitoredDirectoriesTable().convertRowIndexToModel(viewRow);

                    MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) screen.getMonitoredDirectoriesTable().getModel();
                    Optional<MonitoredDirectory> dir = model.getDirectoryAt(modelRow);

                    dir.ifPresent(d -> {
                        JPopupMenu popupMenu = monitoredDirectoryPopupFactory.create(screen.getMonitoredDirectoriesTable(), viewRow);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    });
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
                    Optional<Scan> scan = model.getScanAt(modelRow);

                    if (scan.isPresent()) {
                        NavigationContext context = new NavigationContext();
                        context.put("scanId", scan.get().id());
                        NavigationEvents.navigateTo(Screen.SCAN_SUMMARY, context);
                    }
                }
            }
        });
        // Selection listener for diffs table to show diff details
        screen.getDiffTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && screen.getDiffTable().getSelectedRow() != -1) {
                int viewRow = screen.getDiffTable().getSelectedRow();
                int modelRow = screen.getDiffTable().convertRowIndexToModel(viewRow);

                DiffTableModel model = (DiffTableModel) screen.getDiffTable().getModel();
                Optional<Diff> diff = model.getDiffAt(modelRow);

                diff.ifPresent(d -> screen.getDiffDetails().setText(ReportUtils.formatDiff(d)));
            } else {
                screen.getDiffDetails().setText("");
            }
        });
        screen.getClearFeedButton().addActionListener(e -> clearLiveFeed());
        screen.getFileSearchField().addActionListener(e -> searchForFiles());
        screen.getSearchContainingCheckBox().addActionListener(e -> searchForFiles());
        screen.getDescendingCheckBox().addActionListener(e -> searchForFiles());
        screen.getFilesTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && screen.getFilesTable().getSelectedRow() != -1) {
                int viewRow = screen.getFilesTable().getSelectedRow();
                int modelRow = screen.getFilesTable().convertRowIndexToModel(viewRow);

                FileTableModel model = (FileTableModel) screen.getFilesTable().getModel();
                Optional<File> file = model.getFileAt(modelRow);

                file.ifPresent(this::getSummaryForFile);
            }
        });
        screen.getFileScanSummaryTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = screen.getFileScanSummaryTable().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < fileSummaries.size()) {
                ScanSummary selectedSummary = fileSummaries.get(selectedRow);
                screen.getScanSummaryDetails().setText(ReportUtils.formatSummary(selectedSummary));
            } else {
                screen.getScanSummaryDetails().setText("");
            }
        });
    }

    @Override
    protected void refreshView() {
        // Update UI components based on the current state
        screen.getScanButton().setText(scanRunning ? StringConstants.SCAN_STOP : StringConstants.SCAN_FULL);
        screen.getQuickScanButton().setText(scanRunning ? StringConstants.SCAN_STOP : StringConstants.SCAN_FULL);

        // Scan running views
        boolean showLiveFeed = !screen.getLiveFeedText().getText().isEmpty() || scanRunning;
        boolean showClearLiveFeed = !scanRunning && !screen.getLiveFeedText().getText().isEmpty();
        screen.getScanProgressContainer().setVisible(scanRunning);
        screen.getLiveFeedContainer().setVisible(showLiveFeed);
        screen.getLiveFeedTitle().setVisible(showLiveFeed);
        screen.getLiveFeedDiffCount().setVisible(showLiveFeed);
        screen.getClearFeedButton().setVisible(showClearLiveFeed);

        // File search views
        screen.getSearchResultCount().setText(StringConstants.FILE_SEARCH_RESULTS_PREFIX + (fileResults != null ? fileResults.size() : 0));

        ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans != null ? recentScans : List.of());
        screen.getRecentScanTable().setModel(mostRecentScansListModel);

        MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(allMonitoredDirectories != null ? allMonitoredDirectories : List.of());
        screen.getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);

        DiffTableModel diffTableModel = new DiffTableModel(recentDiffs != null ? recentDiffs : List.of());
        screen.getDiffTable().setModel(diffTableModel);

        FileTableModel fileTableModel = new FileTableModel(fileResults != null ? fileResults : List.of());
        screen.getFilesTable().setModel(fileTableModel);

        ScanSummaryTableModel fileSummaryTableModel = new ScanSummaryTableModel(fileSummaries != null ? fileSummaries : List.of());
        screen.getFileScanSummaryTable().setModel(fileSummaryTableModel);
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
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_START_ERROR + e.getMessage()));
        }
    }

    /**
     * Initiates a scan operation.
     *
     * @param singleDirectory if true, scans only the selected directory; if false, scans all directories.
     */
    public void performStartScan(boolean singleDirectory) {
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
                    Optional<MonitoredDirectory> dir = model.getDirectoryAt(modelRow);
                    if (dir.isPresent()) {
                        startScanSuccess = scanService.startScanById(dir.get().id());
                    } else {
                        startScanSuccess = false;
                    }
                }
            } else {
                startScanSuccess = scanService.startScan();
            }
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    clearLiveFeed();
                    screen.showSuccess(StringConstants.SCAN_STARTED_SUCCESS);
                    startPollingScanLiveFeed(singleDirectory);
                } else {
                    screen.showError(StringConstants.SCAN_STARTED_FAILURE);
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException | StartScanByIdException |
                 JsonProcessingException e) {
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_START_ERROR + e.getMessage()));
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
                    screen.showSuccess(StringConstants.SCAN_STOPPED_SUCCESS);
                } else {
                    screen.showError(StringConstants.SCAN_STOPPED_FAILURE);
                }
            });
        } catch (ExecutionException | InterruptedException | StopScanException e) {
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_STOP_ERROR + e.getMessage()));
        }
    }

    private void onFinishScan(boolean completed, boolean singleDirectory) {
        // Refresh data to display the latest scan results
        fetchDataAndRefreshView();
        // Notify the user about the scan results
        if (completed) {
            int choice;
            // Prompt the user to view scan results based on whether differences were found
            if (totalDiffCount > 0) {
                choice = screen.showOptionDialog(JOptionPane.WARNING_MESSAGE, StringConstants.SCAN_COMPLETED_DIFFS, new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO}, StringConstants.GENERIC_YES);
            } else {
                choice = screen.showOptionDialog(JOptionPane.INFORMATION_MESSAGE, StringConstants.SCAN_COMPLETED_NO_DIFFS, new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO}, StringConstants.GENERIC_YES);
            }

            if (choice == 0) {
                if (singleDirectory) {
                    // If single directory scan, navigate to the scan summary of the most recent scan.
                    try {
                        Scan latestScan = scanService.getMostRecentScans(1).getFirst();
                        NavigationContext context = new NavigationContext();
                        context.put("scanId", latestScan.id());
                        NavigationEvents.navigateTo(Screen.SCAN_SUMMARY, context);
                    } catch (GetMostRecentScansException | ExecutionException | InterruptedException | JsonProcessingException e) {
                        screen.showError(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX + e.getMessage());
                    }
                } else {
                    if (totalDiffCount > 0) {
                        // If full scan, and we have diffs, navigate to the diffs tab to show all differences.
                        screen.getTabbedPane().setSelectedIndex(2);
                    } else {
                        // If no diffs, navigate to the recent scans tab to show the most recent scan.
                        screen.getTabbedPane().setSelectedIndex(0);
                    }
                }
            }
        }  else {
            // Scan did not complete successfully
            screen.showError(StringConstants.SCAN_NOT_COMPLETED);
        }
    }

    /**
     * Clears the live feed text area and resets the total difference count.
     */
    private void clearLiveFeed() {
        // Clear the live feed text area
        screen.getLiveFeedText().setText("");
        // Reset diff count for the next scan
        totalDiffCount = 0;
        // Update the live feed diff count in preparation for the next scan
        screen.getLiveFeedDiffCount().setText(StringConstants.SCAN_DIFFS_PREFIX + totalDiffCount);
        // Refresh the view to reflect the cleared live feed
        refreshView();
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
                screen.getLiveFeedDiffCount().setText(StringConstants.SCAN_DIFFS_PREFIX + totalDiffCount);

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
                SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_LIVE_FEED_ERROR_PREFIX + ex.getMessage()));
                scanStatusTimer.stop(); // Stop polling on error
                onFinishScan(false, singleDirectory);
            }
        });
        scanStatusTimer.start();
    }

    private void searchForFiles() {
        String query = screen.getFileSearchField().getText().trim();
        if (query.isEmpty()) {
            return;
        }

        boolean searchContainingInput = screen.getSearchContainingCheckBox().isSelected();
        boolean descendingOrder = screen.getDescendingCheckBox().isSelected();

        try {
            String searchQuery = searchContainingInput ? "%" + query + "%" : query;
            fileResults = scanSummaryService.searchFiles(searchQuery, !descendingOrder);
            refreshView();
        } catch (GetSearchFilesException | ExecutionException | InterruptedException | JsonProcessingException e) {
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
        }
    }

    private void getSummaryForFile(File file) {
        try {
            fileSummaries = scanSummaryService.getSummaryForFile(file.id());
            refreshView();
        } catch (GetSummaryForFileException | ExecutionException | InterruptedException | JsonProcessingException e) {
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
        }
    }
}
