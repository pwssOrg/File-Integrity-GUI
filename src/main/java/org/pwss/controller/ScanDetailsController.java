package org.pwss.controller;

import java.util.List;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.table.DiffTableModel;
import org.pwss.model.table.SimpleSummaryTableModel;
import org.pwss.model.table.cell.ButtonEditor;
import org.pwss.model.table.cell.ButtonRenderer;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.ScanService;
import org.pwss.service.ScanSummaryService;
import org.pwss.utils.ReportUtils;
import org.pwss.view.screen.ScanDetailsScreen;

/**
 * Controller class that handles operations related to the scan details screen.
 */
public class ScanDetailsController extends BaseController<ScanDetailsScreen> {

    /**
     * Service responsible for managing scan summaries.
     */
    private final ScanSummaryService scanSummaryService;
    /**
     * Service responsible for performing and managing scans.
     */
    private final ScanService scanService;

    /**
     * List of scan summaries. This can be empty if no summaries are available.
     */
    private List<ScanSummary> scanSummaries;
    /**
     * List of differences (diffs) between scans. This can be empty if no diffs are
     * available.
     */
    private List<Diff> diffs;

    /**
     * Constructs a ScanDetailsController with the given screen and initializes
     * services and lists.
     *
     * @param screen The screen instance that this controller will manage
     */
    public ScanDetailsController(ScanDetailsScreen screen) {
        super(screen);
        this.scanSummaryService = new ScanSummaryService();
        this.scanService = new ScanService();
        this.scanSummaries = List.of();
        this.diffs = List.of();
    }

    @Override
    public void onShow() {
        if (scanSummaries != null && !scanSummaries.isEmpty()) {
            // Clear existing data
            scanSummaries = List.of();
        }

        if (diffs != null && !diffs.isEmpty()) {
            // Clear existing data
            diffs = List.of();
        }

        // Fetch data from the service
        fetchData();
    }

    /**
     * Fetches scan summaries and diffs from the service based on the scan ID in the
     * context.
     */
    private void fetchData() {
        try {
            Long scanId = getContext().get("scanId", Long.class);
            if (scanId != null) {
                // Fetch scan summaries for the scan
                scanSummaries = scanSummaryService.getScanSummaryForScan(scanId);
                // Fetch diffs for the scan
                diffs = scanService.getDiffs(scanId, 1000, null, true);
            }
        } catch (Exception e) {
            screen.showError(e.getMessage());
        }
        // Refresh the view with the new data
        refreshView();
    }

    @Override
    void initListeners() {
        // Selection listener for scan summary table
        screen.getScanSummaryTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = screen.getScanSummaryTable().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < scanSummaries.size()) {
                ScanSummary selectedSummary = scanSummaries.get(selectedRow);
                screen.getScanSummaryDetails().setText(ReportUtils.formatSummary(selectedSummary));
            } else {
                screen.getScanSummaryDetails().setText("");
                screen.getDiffDetails().setText("");
            }
        });
        // Selection listener for diff table
        screen.getDiffTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = screen.getDiffTable().getSelectedRow();
            if (selectedRow >= 0 && selectedRow < diffs.size()) {
                Diff selectedDiff = diffs.get(selectedRow);
                screen.getDiffDetails().setText(ReportUtils.formatDiff(selectedDiff));
            } else {
                screen.getDiffDetails().setText("");
            }
        });

        // Back button listener
        screen.getBackButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.HOME));
    }

    @Override
    void refreshView() {
        // Populate scan summary table
        SimpleSummaryTableModel simpleSummaryTableModel = new SimpleSummaryTableModel(scanSummaries);
        screen.getScanSummaryTable().setModel(simpleSummaryTableModel);

        // Populate diffs table
        DiffTableModel diffTableModel = new DiffTableModel(diffs);
        screen.getDiffTable().setModel(diffTableModel);

        screen.getDiffTable().getColumn("Quarantine").setCellRenderer(new ButtonRenderer());
        screen.getDiffTable().getColumn("Quarantine").setCellEditor(new ButtonEditor("⚠️"));
    }
}
