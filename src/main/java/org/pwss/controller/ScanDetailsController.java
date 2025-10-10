package org.pwss.controller;

import java.util.List;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.table.DiffTableModel;
import org.pwss.model.table.SimpleSummaryTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.ScanService;
import org.pwss.service.ScanSummaryService;
import org.pwss.utils.ReportUtils;
import org.pwss.view.screen.ScanDetailsScreen;

public class ScanDetailsController extends BaseController<ScanDetailsScreen> {
    private final ScanSummaryService scanSummaryService;
    private final ScanService scanService;
    private List<ScanSummary> scanSummaries;
    private List<Diff> diffs;

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
     * Fetches scan summaries and diffs from the service based on the scan ID in the context.
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
    }
}
