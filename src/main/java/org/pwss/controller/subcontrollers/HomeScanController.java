package org.pwss.controller.subcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.pwss.controller.HomeController;
import org.pwss.controller.SubController;
import org.pwss.controller.util.NavigationContext;
import org.pwss.exception.scan.GetMostRecentScansException;
import org.pwss.exception.scan.LiveFeedException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.exception.scan.StartFullScanException;
import org.pwss.exception.scan.StartScanByIdException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.response.LiveFeedResponse;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.NoteService;
import org.pwss.utils.ErrorUtils;
import org.pwss.utils.LiveFeedUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.popup_menu.MonitoredDirectoryPopupFactory;
import org.pwss.view.popup_menu.listener.MonitoredDirectoryPopupListenerImpl;
import org.pwss.view.screen.HomeScreen;

public class HomeScanController extends SubController<HomeScreen, HomeController> {
    /**
     * Service for managing notes.
     */
    private final NoteService noteService;
    /**
     * Factory for creating popup menus related to monitored directories.
     */
    private final MonitoredDirectoryPopupFactory monitoredDirectoryPopupFactory;

    /**
     * Total count of differences detected in the scans.
     */
    private final AtomicLong totalDiffCount = new AtomicLong(0);

    /**
     * Timer for checking the status of ongoing scans at regular intervals.
     */
    private Timer scanStatusTimer;

    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public HomeScanController(HomeController parentController, HomeScreen screen) {
        super(parentController, screen);
        this.noteService = new NoteService();
        this.monitoredDirectoryPopupFactory = new MonitoredDirectoryPopupFactory(
                new MonitoredDirectoryPopupListenerImpl(this, monitoredDirectoryService, noteService));
    }

    @Override
    protected void initListeners() {
        // Full scan button listener
        screen.getScanButton().addActionListener(e -> handleScanButtonClick(false));
        // Clear live feed button listener
        screen.getClearFeedButton().addActionListener(e -> clearLiveFeed());
        // Monitored directories table popup menu listener
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

                    MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) screen
                            .getMonitoredDirectoriesTable().getModel();
                    Optional<MonitoredDirectory> dir = model.getDirectoryAt(modelRow);

                    dir.ifPresent(d -> {
                        JPopupMenu popupMenu = monitoredDirectoryPopupFactory
                                .create(screen.getMonitoredDirectoriesTable(), viewRow);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    });
                }
            }
        });
    }

    @Override
    public void reloadData() {
        super.reloadData();
    }

    @Override
    protected void refreshView() {
        // Scan running views
        screen.getScanButton().setText(scanRunning ? StringConstants.SCAN_STOP : StringConstants.SCAN_FULL);

        boolean showLiveFeed = !screen.getLiveFeedText().getText().isEmpty() || scanRunning;
        boolean showClearLiveFeed = !scanRunning && !screen.getLiveFeedText().getText().isEmpty();

        screen.getLiveFeedContainer().setVisible(showLiveFeed);
        screen.getLiveFeedTitle().setVisible(showLiveFeed);
        screen.getLiveFeedDiffCount().setVisible(showLiveFeed);
        screen.getClearFeedButton().setVisible(showClearLiveFeed);

        // Monitored directories table model
        MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(
                allMonitoredDirectories != null ? allMonitoredDirectories : List.of());
        screen.getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
        screen.getMonitoredDirectoriesTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (parentController.scanRunning) {
            startPollingScanLiveFeed(false, Collections.emptyList());
        }
    }

    /**
     * Handles the logic when the scan button is clicked.
     *
     * @param singleDirectory if true, scans only the selected directory; if false,
     *                        scans all directories.
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
            log.debug(StringConstants.SCAN_START_ERROR, e);
            log.error(StringConstants.SCAN_START_ERROR + " {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_START_ERROR));
        }
    }

    /**
     * Initiates a scan operation.
     *
     * @param singleDirectory if true, scans only the selected directory; if false,
     *                        scans all directories.
     */
    public void performStartScan(boolean singleDirectory) {
        try {
            boolean startScanSuccess;
            boolean baseLineScan;
            JTable table = screen.getMonitoredDirectoriesTable();
            MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) table.getModel();
            ArrayList<MonitoredDirectory> scanningDirs = new ArrayList<>();
            if (singleDirectory) {
                int viewRow = table.getSelectedRow();
                if (viewRow == -1) {
                    startScanSuccess = false;
                    baseLineScan = false;
                } else {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    Optional<MonitoredDirectory> dir = model.getDirectoryAt(modelRow);
                    if (dir.isPresent()) {
                        startScanSuccess = scanService.startScanById(dir.get().id(), maxFileSizeForHashExtraction);
                        scanningDirs.add(dir.get());
                        baseLineScan = !dir.get().baselineEstablished();
                    } else {
                        baseLineScan = false;
                        startScanSuccess = false;
                    }
                }
            } else {
                baseLineScan = false;
                startScanSuccess = scanService.startScan(maxFileSizeForHashExtraction);
                scanningDirs.addAll(allMonitoredDirectories);
            }
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    clearLiveFeed();
                    if (baseLineScan) {
                        screen.showSuccess(StringConstants.SCAN_STARTED_BASELINE_SUCCESS);
                    } else {
                        log.info(StringConstants.SCAN_STARTED_SUCCESS);
                    }
                    startPollingScanLiveFeed(singleDirectory, scanningDirs);
                } else {
                    screen.showError(StringConstants.SCAN_STARTED_FAILURE);
                }
            });
        } catch (ExecutionException | InterruptedException | StartFullScanException | StartScanByIdException
                 | JsonProcessingException e) {
            log.debug(StringConstants.SCAN_START_ERROR, e);
            log.error(StringConstants.SCAN_START_ERROR + " {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen
                    .showError(ErrorUtils.formatErrorMessage(StringConstants.SCAN_START_ERROR, e.getMessage())));
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
            log.debug(StringConstants.SCAN_STOP_ERROR, e);
            log.error(StringConstants.SCAN_STOP_ERROR + " {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_STOP_ERROR));
        }
    }

    private void onFinishScan(boolean completed, boolean singleDirectory) {
        // Refresh data to display the latest scan results
        fetchDataAndRefreshView();
        // Notify the user about the scan results
        if (completed) {
            int choice;
            // Prompt the user to view scan results based on whether differences were found
            if (totalDiffCount.get() > 0) {
                choice = screen.showOptionDialog(JOptionPane.WARNING_MESSAGE, StringConstants.SCAN_COMPLETED_DIFFS,
                        new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO},
                        StringConstants.GENERIC_YES);
            } else {
                String message;
                if (singleDirectory) {
                    try {
                        // If single directory scan, check if it was a baseline scan
                        Scan latestScan = scanService.getMostRecentScans(1).getFirst();
                        if (latestScan.isBaselineScan()) {
                            message = StringConstants.SCAN_BASELINE_COMPLETED;
                        } else {
                            message = StringConstants.SCAN_COMPLETED_NO_DIFFS;
                        }
                    } catch (GetMostRecentScansException | ExecutionException | InterruptedException
                             | JsonProcessingException e) {
                        log.error(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX, e.getMessage());
                        log.debug(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX, e);
                        screen.showError(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX);
                        return;
                    }
                } else {
                    message = StringConstants.SCAN_COMPLETED_NO_DIFFS;
                }
                choice = screen.showOptionDialog(JOptionPane.INFORMATION_MESSAGE,
                        message,
                        new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO},
                        StringConstants.GENERIC_YES);
            }

            if (choice == 0) {
                if (singleDirectory) {
                    // If single directory scan, navigate to the scan summary of the most recent
                    // scan.
                    try {
                        Scan latestScan = scanService.getMostRecentScans(1).getFirst();
                        NavigationContext context = new NavigationContext();
                        context.put("scanId", latestScan.id());
                        NavigationEvents.navigateTo(Screen.SCAN_SUMMARY, context);
                    } catch (GetMostRecentScansException | ExecutionException | InterruptedException
                             | JsonProcessingException e) {
                        log.error(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX, e.getMessage());
                        log.debug(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX, e);
                        screen.showError(StringConstants.SCAN_SHOW_RESULTS_ERROR_PREFIX);
                    }
                } else {
                    if (totalDiffCount.get() > 0) {
                        // If full scan, and we have diffs, navigate to the diffs tab to show all
                        // differences.
                        screen.getTabbedPane().setSelectedIndex(2);
                    } else {
                        // If no diffs, navigate to the recent scans tab to show the most recent scan.
                        screen.getTabbedPane().setSelectedIndex(0);
                    }
                }
            }
        } else {
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
        totalDiffCount.set(0);
        // Update the live feed diff count in preparation for the next scan
        screen.getLiveFeedDiffCount().setText(StringConstants.SCAN_DIFFS_PREFIX + totalDiffCount);
        // Refresh the view to reflect the cleared live feed
        refreshView();
    }

    /**
     * Starts polling the live feed for scan updates.
     * This method sets up a timer to periodically fetch live feed updates
     * and update the UI accordingly.
     *
     * @param singleDirectory if true, indicates that the scan is for a single
     *                        directory; otherwise, for all directories.
     * @param scanningDirs    the list of directories being scanned
     */
    private void startPollingScanLiveFeed(boolean singleDirectory, List<MonitoredDirectory> scanningDirs) {
        if (scanStatusTimer != null && scanStatusTimer.isRunning()) {
            return; // Polling is already active
        }

        // Log directories that are establishing their baseline
        for (MonitoredDirectory dir : scanningDirs) {
            if (!dir.baselineEstablished()) {
                log.debug("Establishing baseline for {}", dir.path());
                String currentLiveFeedText = screen.getLiveFeedText().getText();
                String newEntry = StringConstants.SCAN_ESTABLISHING_BASELINE + dir.path() + "\n";
                String updatedLiveFeedText = currentLiveFeedText + newEntry;
                screen.getLiveFeedText().setText(updatedLiveFeedText);
            }
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
                totalDiffCount.addAndGet(LiveFeedUtils.countWarnings(liveFeed.livefeed()));
                screen.getLiveFeedDiffCount().setText(StringConstants.SCAN_DIFFS_PREFIX + totalDiffCount);

                // Update scanRunning state and refresh the UI if necessary
                if (liveFeed.isScanRunning() != scanRunning) {
                    scanRunning = liveFeed.isScanRunning();
                    refreshView();
                }
                if (!liveFeed.isScanRunning()) {

                    scanStatusTimer.stop(); // Terminate polling when the scan completes

                    liveFeed = scanService.getLiveFeed();

                    totalDiffCount.getAndAdd(LiveFeedUtils.countWarnings(liveFeed.livefeed()));
                    screen.getLiveFeedDiffCount().setText(StringConstants.SCAN_DIFFS_PREFIX + totalDiffCount);

                    onFinishScan(true, singleDirectory);
                }
            } catch (LiveFeedException | ExecutionException | InterruptedException | JsonProcessingException ex) {
                log.error(StringConstants.SCAN_LIVE_FEED_ERROR_PREFIX + " {}", ex.getMessage());
                log.debug("Debug Live Feed Exception", ex);
                SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_LIVE_FEED_ERROR_PREFIX));
                scanStatusTimer.stop(); // Stop polling on error
                onFinishScan(false, singleDirectory);
            }
        });
        scanStatusTimer.start();
    }
}
