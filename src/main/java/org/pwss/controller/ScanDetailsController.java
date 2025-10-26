package org.pwss.controller;

import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import org.pwss.model.entity.Diff;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.table.DiffTableModel;
import org.pwss.model.table.SimpleSummaryTableModel;
import org.pwss.model.table.cell.ButtonEditor;
import org.pwss.model.table.cell.ButtonRenderer;
import org.pwss.model.table.cell.CellButtonListener;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.service.FileService;
import org.pwss.service.ScanService;
import org.pwss.service.ScanSummaryService;
import org.pwss.utils.OSUtils;
import org.pwss.utils.ReportUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.ScanDetailsScreen;

/**
 * Controller class that handles operations related to the scan details screen.
 */
public class ScanDetailsController extends BaseController<ScanDetailsScreen> implements CellButtonListener {

    /**
     * Service responsible for managing scan summaries.
     */
    private final ScanSummaryService scanSummaryService;

    /**
     * Service for quarantine file operations.
     */
    private final FileService fileService;
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
        this.fileService = new FileService();
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

        screen.getDiffTable().getColumn(DiffTableModel.columns[3]).setCellRenderer(new ButtonRenderer());
        screen.getDiffTable().getColumn(DiffTableModel.columns[3]).setCellEditor(new ButtonEditor("\uD83D\uDCE5", this));
    }

    @Override
    public void onCellButtonClicked(int row, int column) {
        DiffTableModel model = (DiffTableModel) screen.getDiffTable().getModel();
        Optional<Diff> diff = model.getDiffAt(row);

        diff.ifPresent(d -> {
            int choice = screen.showOptionDialog(
                    JOptionPane.WARNING_MESSAGE,
                    OSUtils.getQuarantineWarningMessage(),
                    new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO},
                    StringConstants.GENERIC_NO
            );
            if (choice == 0) {
                try {
                    boolean success = fileService.quarantineFile(d.integrityFail().file().id());
                    if (success) {
                        screen.showInfo("File quarantined successfully.");
                    } else {
                        screen.showError("Failed to quarantine the file.");
                    }
                } catch (Exception e) {
                    screen.showError(e.getMessage());
                }
            }
        });
    }
}
