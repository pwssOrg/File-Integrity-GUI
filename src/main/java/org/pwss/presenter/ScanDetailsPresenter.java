package org.pwss.presenter;

import org.pwss.model.entity.ScanSummary;
import org.pwss.model.service.ScanSummaryService;
import org.pwss.model.table.ScanSummaryTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.view.screen.ScanDetailsScreen;

import java.util.List;

public class ScanDetailsPresenter extends BasePresenter<ScanDetailsScreen> {
    private final ScanSummaryService scanSummaryService;
    private List<ScanSummary> scanSummaries;

    public ScanDetailsPresenter(ScanDetailsScreen screen) {
        super(screen);
        this.scanSummaryService = new ScanSummaryService();
    }

    @Override
    public void onShow() {
        // Clear existing data
        scanSummaries.clear();
        // Fetch data from the service
        fetchData();
    }

    private void fetchData() {
        try {
            Long scanId = getContext().get("scanId", Long.class);
            scanSummaries = scanSummaryService.getScanSummaryForScan(scanId);
        } catch (Exception e) {
            screen.showError("Failed to fetch scan data: " + e.getMessage());
        }
        refreshView();
    }

    @Override
    void initListeners() {
        screen.getBackButton().addActionListener(e -> NavigationEvents.navigateTo(Screen.HOME, null));
    }

    @Override
    void refreshView() {
        ScanSummaryTableModel scanSummaryTableModel = new ScanSummaryTableModel(scanSummaries);
        screen.getScanSummaryTable().setModel(scanSummaryTableModel);
    }
}
