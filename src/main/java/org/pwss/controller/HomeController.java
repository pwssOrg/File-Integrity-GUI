package org.pwss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.pwss.app_settings.AppConfig;
import org.pwss.controller.util.NavigationContext;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.GetAllMostRecentScansException;
import org.pwss.exception.scan.GetMostRecentScansException;
import org.pwss.exception.scan.GetScanDiffsException;
import org.pwss.exception.scan.LiveFeedException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.exception.scan.StartScanAllException;
import org.pwss.exception.scan.StartScanByIdException;
import org.pwss.exception.scan.StopScanException;
import org.pwss.exception.scan_summary.GetSearchFilesException;
import org.pwss.exception.scan_summary.GetSummaryForFileException;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.File;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.response.LiveFeedResponse;
import org.pwss.model.table.DiffTableModel;
import org.pwss.model.table.FileTableModel;
import org.pwss.model.table.MonitoredDirectoryTableModel;
import org.pwss.model.table.ScanSummaryTableModel;
import org.pwss.model.table.ScanTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.service.NoteService;
import org.pwss.service.ScanService;
import org.pwss.service.ScanSummaryService;
import org.pwss.service.network.PwssHttpClient;
import org.pwss.utils.AppTheme;
import org.pwss.utils.LiveFeedUtils;
import org.pwss.utils.MonitoredDirectoryUtils;
import org.pwss.utils.ReportUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.popup_menu.MonitoredDirectoryPopupFactory;
import org.pwss.view.popup_menu.listener.MonitoredDirectoryPopupListenerImpl;
import org.pwss.view.screen.HomeScreen;
import org.slf4j.LoggerFactory;


import static org.pwss.app_settings.AppConfig.APP_THEME;

public class HomeController extends BaseController<HomeScreen> {

    /**
     * Logger for logging messages within this controller.
     */
    /**
     * Logger for logging messages within this controller.
     */
    private final org.slf4j.Logger log = LoggerFactory.getLogger(HomeController.class);

    /**
     * Service to handle scan-related operations.
     */
    private final ScanService scanService;

    /**
     * Service to manage monitored directories.
     */
    private final MonitoredDirectoryService monitoredDirectoryService;

    /**
     * Service for retrieving and managing scan summaries.
     */
    private final ScanSummaryService scanSummaryService;

    /**
     * Service for managing notes.
     */
    private final NoteService noteService;

    /**
     * Factory for creating popup menus related to monitored directories.
     */
    private final MonitoredDirectoryPopupFactory monitoredDirectoryPopupFactory;

    /**
     * List of all monitored directories.
     */
    private List<MonitoredDirectory> allMonitoredDirectories;

    /**
     * List of recent scans performed.
     */
    private List<Scan> recentScans;

    /**
     * List of most recent differences detected in the scans.
     */
    private List<Diff> recentDiffs;

    /**
     * List of files resulting from search operations.
     */
    private List<File> fileResults;

    /**
     * List of scan summaries for specific files.
     */
    private List<ScanSummary> fileSummaries;

    /**
     * Flag indicating whether a scan is currently running.
     */
    private boolean scanRunning;

    /**
     * Total count of differences detected in the scans.
     */
    private long totalDiffCount = 0;

    /**
     * Timer for checking the status of ongoing scans at regular intervals.
     */
    private Timer scanStatusTimer;

    /**
     * Constructor to initialize HomeController with a HomeScreen view instance.
     *
     * @param view The home screen view that this controller will manage.
     */
    public HomeController(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
        this.scanSummaryService = new ScanSummaryService();
        this.noteService = new NoteService();
        this.monitoredDirectoryPopupFactory = new MonitoredDirectoryPopupFactory(
                new MonitoredDirectoryPopupListenerImpl(this, monitoredDirectoryService, noteService));
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
            // Fetch all monitored directories for display in the monitored directories
            // table
            allMonitoredDirectories = monitoredDirectoryService.getAllDirectories();

            // Only fetch diffs if there are monitored directories present
            if (!allMonitoredDirectories.isEmpty()) {
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
            }
            // Check if a scan is currently running
            boolean scanCurrentlyRunning = scanService.scanRunning();
            // If a scan has started since the last check, initiate polling
            if (scanCurrentlyRunning && !scanRunning) {
                scanRunning = true;
                startPollingScanLiveFeed(false, Collections.emptyList());
            }
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException | JsonProcessingException
                | GetAllMostRecentScansException | ScanStatusException e) {
            log.error("Error getting data: {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("Error getting data"));
        }
        // Finally, refresh the view to reflect the updated data
        refreshView();
    }

    /**
     * Safely retrieves diffs for a given scan ID.
     * This method handles exceptions and returns an empty list in case of errors.
     *
     * @param scanId the ID of the scan for which to retrieve diffs
     * @return a list of diffs associated with the scan, or an empty list if an
     *         error occurs
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
        screen.getAddNewDirectoryButton()
                .addActionListener(e -> NavigationEvents.navigateTo(Screen.NEW_DIRECTORY));
        screen.getScanButton().addActionListener(e -> handleScanButtonClick(false));
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
        screen.getThemePicker().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppTheme selectedTheme = (AppTheme) e.getItem();
                if (selectedTheme != null) {
                    log.debug("Selected theme: {}", selectedTheme.getDisplayName());
                    boolean result = AppConfig.setAppTheme(selectedTheme.getValue());
                    if (result) {
                        log.debug("Theme changed to {}. Restart application to apply.", selectedTheme.getDisplayName());
                    } else {
                        log.error("Failed to change theme to {}", selectedTheme.getDisplayName());
                    }
                    // Set FlatLaf Look and Feel
                    try {
                        if (AppConfig.APP_THEME == 1)
                            UIManager.setLookAndFeel(new FlatDarculaLaf());
                        else if (AppConfig.APP_THEME == 2)
                            UIManager.setLookAndFeel(new FlatLightLaf());
                        else if (AppConfig.APP_THEME == 3)
                            UIManager.setLookAndFeel(new FlatMacLightLaf());
                        else if (AppConfig.APP_THEME == 4)
                            UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    } catch (Exception ex) {
                        log.debug("Failed to initialize LaF", ex);
                        log.error("Failed to initialize LaF", ex.getMessage());
                        SwingUtilities.invokeLater(() -> screen.showError("Failed to apply theme"));
                    }
                }
            }
        });
        screen.getLogoutButton().addActionListener(e -> {
            PwssHttpClient.getInstance().clearSession();
            NavigationEvents.navigateTo(Screen.LOGIN);
        });
    }

    @Override
    protected void refreshView() {
        // Update UI components based on the current state
        screen.getScanButton().setText(scanRunning ? StringConstants.SCAN_STOP : StringConstants.SCAN_FULL);

        String notifications = MonitoredDirectoryUtils.getMonitoredDirectoryNotificationMessage(allMonitoredDirectories);
        boolean hasNotifications = !notifications.isEmpty();

        // Scan running views
        boolean showLiveFeed = !screen.getLiveFeedText().getText().isEmpty() || scanRunning;
        boolean showClearLiveFeed = !scanRunning && !screen.getLiveFeedText().getText().isEmpty();
        screen.getScanProgressContainer().setVisible(scanRunning);
        screen.getLiveFeedContainer().setVisible(showLiveFeed);
        screen.getLiveFeedTitle().setVisible(showLiveFeed);
        screen.getLiveFeedDiffCount().setVisible(showLiveFeed);
        screen.getClearFeedButton().setVisible(showClearLiveFeed);
        screen.getNotificationPanel().setVisible(hasNotifications);

        // Set notification text area
        screen.getNotificationTextArea().setText(notifications);

        // File search views
        screen.getSearchResultCount()
                .setText(StringConstants.FILE_SEARCH_RESULTS_PREFIX + (fileResults != null ? fileResults.size() : 0));

        ScanTableModel mostRecentScansListModel = new ScanTableModel(recentScans != null ? recentScans : List.of());
        screen.getRecentScanTable().setModel(mostRecentScansListModel);
        screen.getRecentScanTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel<MonitoredDirectory> monitoredDirsModel = new DefaultListModel<>();

        if (allMonitoredDirectories != null) {
            allMonitoredDirectories.forEach(monitoredDirsModel::addElement);
        }

        screen.getMonitoredDirectoryList().setModel(monitoredDirsModel);

        screen.getMonitoredDirectoryList().setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof MonitoredDirectory dir) {
                    setText(dir.path());
                    if (!dir.baselineEstablished()) {
                        setForeground(Color.YELLOW);
                        setToolTipText(StringConstants.TOOLTIP_BASELINE_NOT_ESTABLISHED);
                    } else if (MonitoredDirectoryUtils.isScanOlderThanAWeek(dir)) {
                        setForeground(Color.ORANGE);
                        setToolTipText(StringConstants.TOOLTIP_OLD_SCAN);
                    } else {
                        setToolTipText(dir.path());
                    }
                }
                return this;
            }
        });

        MonitoredDirectoryTableModel monitoredDirectoryTableModel = new MonitoredDirectoryTableModel(
                allMonitoredDirectories != null ? allMonitoredDirectories : List.of());
        screen.getMonitoredDirectoriesTable().setModel(monitoredDirectoryTableModel);
        screen.getMonitoredDirectoriesTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DiffTableModel diffTableModel = new DiffTableModel(recentDiffs != null ? recentDiffs : List.of());
        screen.getDiffTable().setModel(diffTableModel);
        screen.getDiffTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        FileTableModel fileTableModel = new FileTableModel(fileResults != null ? fileResults : List.of());
        screen.getFilesTable().setModel(fileTableModel);
        screen.getFilesTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ScanSummaryTableModel fileSummaryTableModel = new ScanSummaryTableModel(
                fileSummaries != null ? fileSummaries : List.of());
        screen.getFileScanSummaryTable().setModel(fileSummaryTableModel);
        screen.getFileScanSummaryTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Update theme picker
        screen.getThemePicker().removeAllItems();
        // Populate the combo box with AppTheme values
        for (AppTheme theme : AppTheme.values()) {
            screen.getThemePicker().addItem(theme);
        }
        // Set the selected item based on the current app theme
        screen.getThemePicker().setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AppTheme) {
                    setText(((AppTheme) value).getDisplayName());
                }
                return this;
            }
        });
        // Select the current theme in the combo box
        for (AppTheme theme : AppTheme.values()) {
            if (theme.getValue() == APP_THEME) {
                screen.getThemePicker().setSelectedItem(theme);
                break;
            }
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
            JTable table = screen.getMonitoredDirectoriesTable();
            MonitoredDirectoryTableModel model = (MonitoredDirectoryTableModel) table.getModel();
            ArrayList<MonitoredDirectory> scanningDirs = new ArrayList<>();
            if (singleDirectory) {
                int viewRow = table.getSelectedRow();
                if (viewRow == -1) {
                    startScanSuccess = false;
                } else {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    Optional<MonitoredDirectory> dir = model.getDirectoryAt(modelRow);
                    if (dir.isPresent()) {
                        startScanSuccess = scanService.startScanById(dir.get().id());
                        scanningDirs.add(dir.get());
                    } else {
                        startScanSuccess = false;
                    }
                }
            } else {
                startScanSuccess = scanService.startScan();
                scanningDirs.addAll(allMonitoredDirectories);
            }
            SwingUtilities.invokeLater(() -> {
                if (startScanSuccess) {
                    clearLiveFeed();
                    screen.showSuccess(StringConstants.SCAN_STARTED_SUCCESS);
                    startPollingScanLiveFeed(singleDirectory, scanningDirs);
                } else {
                    screen.showError(StringConstants.SCAN_STARTED_FAILURE);
                }
            });
        } catch (ExecutionException | InterruptedException | StartScanAllException | StartScanByIdException
                | JsonProcessingException e) {
            log.debug(StringConstants.SCAN_START_ERROR, e);
            log.error(StringConstants.SCAN_START_ERROR + " {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_START_ERROR));
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
            if (totalDiffCount > 0) {
                choice = screen.showOptionDialog(JOptionPane.WARNING_MESSAGE, StringConstants.SCAN_COMPLETED_DIFFS,
                        new String[] { StringConstants.GENERIC_YES, StringConstants.GENERIC_NO },
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
                        new String[] { StringConstants.GENERIC_YES, StringConstants.GENERIC_NO },
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
                    if (totalDiffCount > 0) {
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
                log.error(StringConstants.SCAN_LIVE_FEED_ERROR_PREFIX + " {}", ex.getMessage());
                log.debug("Debug Live Feed Exception", ex);
                SwingUtilities.invokeLater(() -> screen.showError(StringConstants.SCAN_LIVE_FEED_ERROR_PREFIX));
                scanStatusTimer.stop(); // Stop polling on error
                onFinishScan(false, singleDirectory);
            }
        });
        scanStatusTimer.start();
    }

    /**
     * Searches for files based on user input and updates the view with the results.
     * This method retrieves the search query and options from the UI, performs
     * the search using the ScanSummaryService, and refreshes the view to display
     * the search results.
     */
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
            log.error("Error when searching for files: {}", e.getMessage());
            log.debug("Debug File Search Exception", e);
            SwingUtilities.invokeLater(() -> screen.showError(e.getMessage()));
        }
    }

    /**
     * Retrieves scan summaries for a specific file and updates the view.
     * This method fetches the scan summaries associated with the given file
     * using the ScanSummaryService and refreshes the view to display the summaries.
     *
     * @param file the file for which to retrieve scan summaries
     */
    private void getSummaryForFile(File file) {
        try {
            fileSummaries = scanSummaryService.getSummaryForFile(file.id());
            refreshView();
        } catch (GetSummaryForFileException | ExecutionException | InterruptedException | JsonProcessingException e) {
            log.error("Error when getting summaries for a file: {}", e.getMessage());
            log.debug("Debug Getting Summaries for a file Exception", e);
            SwingUtilities.invokeLater(() -> screen.showError("Error getting summaries for a file"));
        }
    }
}
