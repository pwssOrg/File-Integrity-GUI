package org.pwss.controller.subcontrollers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import org.pwss.controller.HomeController;
import org.pwss.controller.SubController;
import org.pwss.controller.util.NavigationContext;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Scan;
import org.pwss.model.table.ScanTableModel;
import org.pwss.navigation.NavigationEvents;
import org.pwss.navigation.Screen;
import org.pwss.utils.MonitoredDirectoryUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.HomeScreen;

public class HomeMainController extends SubController<HomeScreen, HomeController> {
    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public HomeMainController(HomeController parentController, HomeScreen screen) {
        super(parentController, screen);
    }

    @Override
    protected void initListeners() {
        // Listener for "Add New Directory" button
        screen.getAddNewDirectoryButton()
                .addActionListener(e -> NavigationEvents.navigateTo(Screen.NEW_DIRECTORY));

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
    }

    @Override
    protected void refreshView() {
        String notifications = MonitoredDirectoryUtils
                .getMonitoredDirectoryNotificationMessage(parentController.allMonitoredDirectories);
        boolean hasNotifications = !notifications.isEmpty();

        // Set notification text area
        screen.getNotificationPanel().setVisible(hasNotifications);
        screen.getNotificationTextArea().setText(notifications);

        // Populate recent scans table
        ScanTableModel mostRecentScansListModel = new ScanTableModel(parentController.recentScans != null ? parentController.recentScans : List.of());
        screen.getRecentScanTable().setModel(mostRecentScansListModel);
        screen.getRecentScanTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Populate monitored directories list
        DefaultListModel<MonitoredDirectory> monitoredDirsModel = new DefaultListModel<>();
        if (parentController.allMonitoredDirectories != null) {
            parentController.allMonitoredDirectories.forEach(monitoredDirsModel::addElement);
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
    }

}
