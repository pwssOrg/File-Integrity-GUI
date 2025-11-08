package org.pwss.controller.subcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.pwss.controller.HomeController;
import org.pwss.controller.SubController;
import org.pwss.exception.scan_summary.FileSearchException;
import org.pwss.exception.scan_summary.GetSummaryForFileException;
import org.pwss.model.entity.File;
import org.pwss.model.entity.ScanSummary;
import org.pwss.model.table.FileTableModel;
import org.pwss.model.table.ScanSummaryTableModel;
import org.pwss.service.ScanSummaryService;
import org.pwss.utils.ReportUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.HomeScreen;

public class HomeFilesController extends SubController<HomeScreen, HomeController> {
    /**
     * Service for retrieving and managing scan summaries.
     */
    private final ScanSummaryService scanSummaryService;
    /**
     * List of files resulting from search operations.
     */
    private List<File> fileResults;
    /**
     * List of scan summaries for specific files.
     */
    private List<ScanSummary> fileSummaries;

    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public HomeFilesController(HomeController parentController, HomeScreen screen) {
        super(parentController, screen);
        this.scanSummaryService = new ScanSummaryService();
    }

    @Override
    protected void initListeners() {
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
        // File search views
        screen.getSearchResultCount()
                .setText(StringConstants.FILE_SEARCH_RESULTS_PREFIX + (fileResults != null ? fileResults.size() : 0));

        FileTableModel fileTableModel = new FileTableModel(fileResults != null ? fileResults : List.of());
        screen.getFilesTable().setModel(fileTableModel);
        screen.getFilesTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ScanSummaryTableModel fileSummaryTableModel = new ScanSummaryTableModel(
                fileSummaries != null ? fileSummaries : List.of());
        screen.getFileScanSummaryTable().setModel(fileSummaryTableModel);
        screen.getFileScanSummaryTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        } catch (FileSearchException | ExecutionException | InterruptedException | JsonProcessingException e) {
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
