package org.pwss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import org.pwss.controller.subcontrollers.HomeDiffsController;
import org.pwss.controller.subcontrollers.HomeFilesController;
import org.pwss.controller.subcontrollers.HomeMainController;
import org.pwss.controller.subcontrollers.HomeScanController;
import org.pwss.controller.subcontrollers.HomeSettingsController;
import org.pwss.exception.metadata.MetadataKeyNameRetrievalException;
import org.pwss.exception.monitored_directory.MonitoredDirectoryGetAllException;
import org.pwss.exception.scan.GetAllMostRecentScansException;
import org.pwss.exception.scan.GetScanDiffsException;
import org.pwss.exception.scan.ScanStatusException;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.QuarantineMetadata;
import org.pwss.model.entity.Scan;
import org.pwss.service.AppService;
import org.pwss.service.FileService;
import org.pwss.service.MonitoredDirectoryService;
import org.pwss.service.NoteService;
import org.pwss.service.ScanService;
import org.pwss.service.ScanSummaryService;
import org.pwss.view.screen.HomeScreen;
import org.slf4j.LoggerFactory;


import static org.pwss.app_settings.AppConfig.MAX_HASH_EXTRACTION_FILE_SIZE;
import static org.pwss.app_settings.AppConfig.USE_SPLASH_SCREEN;

public final class HomeController extends BaseController<HomeScreen> {

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
     * List of all monitored directories.
     */
    public List<MonitoredDirectory> allMonitoredDirectories;
    /**
     * List of recent scans performed.
     */
    public List<Scan> recentScans;
    /**
     * List of most recent differences detected in the scans.
     */
    public List<Diff> recentDiffs;
    /**
     * Flag indicating whether a scan is currently running.
     */
    public boolean scanRunning;

    /**
     * Constructor to initialize HomeController with a HomeScreen view instance.
     *
     * @param view The home screen view that this controller will manage.
     */
    public HomeController(HomeScreen view) {
        super(view);
        this.scanService = new ScanService();
        this.monitoredDirectoryService = new MonitoredDirectoryService();
    }

    @Override
    void onCreate() {
        // Initialize sub-controllers for different sections of the home screen
        addSubController(new HomeMainController(this, screen));
        addSubController(new HomeScanController(this, screen));
        addSubController(new HomeFilesController(this, screen));
        addSubController(new HomeDiffsController(this, screen));
        addSubController(new HomeSettingsController(this, screen));
    }

    @Override
    public void onShow() {
        super.onShow();
        reloadData();
    }

    @Override
    public void reloadData() {
        try {
            // Fetch all monitored directories for display in the monitored directories
            // table
            allMonitoredDirectories = monitoredDirectoryService.getAllDirectories();

            // Only fetch diffs if there are active monitored directories present
            final long activeDirCount = allMonitoredDirectories.stream().filter(MonitoredDirectory::isActive).count();
            if (activeDirCount > 0) {
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
            }
        } catch (MonitoredDirectoryGetAllException | ExecutionException | InterruptedException | JsonProcessingException
                 | GetAllMostRecentScansException | ScanStatusException e) {
            log.error("Error getting data: {}", e.getMessage());
            SwingUtilities.invokeLater(() -> screen.showError("Error getting data"));
        }
        super.reloadData();
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
    protected void initListeners() {
        // No specific listeners at the HomeController level; sub-controllers handle their own listeners
    }

    @Override
    protected void refreshView() {
        screen.getScanProgressContainer().setVisible(scanRunning);
    }
}
