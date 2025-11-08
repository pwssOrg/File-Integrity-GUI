package org.pwss.controller.subcontrollers;

import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import org.pwss.controller.HomeController;
import org.pwss.controller.SubController;
import org.pwss.model.entity.Diff;
import org.pwss.model.table.DiffTableModel;
import org.pwss.model.table.cell.ButtonEditor;
import org.pwss.model.table.cell.ButtonRenderer;
import org.pwss.utils.OSUtils;
import org.pwss.utils.ReportUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.HomeScreen;

public class HomeDiffsController extends SubController<HomeScreen, HomeController> {
    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public HomeDiffsController(HomeController parentController, HomeScreen screen) {
        super(parentController, screen);
    }

    @Override
    protected void initListeners() {
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
    }

    @Override
    protected void refreshView() {
        final List<Diff> recentDiffs = parentController.recentDiffs;

        DiffTableModel diffTableModel = new DiffTableModel(recentDiffs != null ? recentDiffs : List.of());
        screen.getDiffTable().setModel(diffTableModel);
        screen.getDiffTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        screen.getDiffTable().getColumn(DiffTableModel.columns[3]).setCellRenderer(new ButtonRenderer());
        screen.getDiffTable().getColumn(DiffTableModel.columns[3])
                .setCellEditor(new ButtonEditor("\uD83D\uDCE5", (row, column) -> {
                    DiffTableModel model = (DiffTableModel) screen.getDiffTable().getModel();
                    Optional<Diff> diff = model.getDiffAt(row);

                    diff.ifPresent(d -> {
                        int choice = screen.showOptionDialog(
                                JOptionPane.WARNING_MESSAGE,
                                OSUtils.getQuarantineWarningMessage(),
                                new String[] { StringConstants.GENERIC_YES, StringConstants.GENERIC_NO },
                                StringConstants.GENERIC_NO);
                        if (choice == 0) {
                            try {
                                boolean success = fileService.quarantineFile(d.integrityFail().file().id());
                                if (success) {
                                    screen.showInfo("File quarantined successfully.");
                                    parentController.reloadData();
                                } else {
                                    screen.showError("Failed to quarantine the file.");
                                }
                            } catch (Exception e) {
                                screen.showError(e.getMessage());
                            }
                        }
                    });
                }));
    }
}
