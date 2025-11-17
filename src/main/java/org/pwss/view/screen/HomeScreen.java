package org.pwss.view.screen;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.util.AppTheme;

/**
 * The HomeScreen class represents the main screen of the application.
 * It extends BaseScreen and contains various UI components that make up
 * the home screen's user interface. These components include panels,
 * buttons, tables, progress bars, text areas, and more.
 */
public class HomeScreen extends BaseScreen {
    /**
     * The root panel containing all other UI components.
     */
    private JPanel rootPanel;

    /**
     * A tabbed pane that allows switching between different tabs in the home
     * screen.
     */
    private JTabbedPane tabbedPane;

    /**
     * Panel representing the home tab of the application.
     */
    private JPanel homeTab;

    /**
     * Panel representing the scan tab of the application.
     */
    private JPanel scanTab;

    /**
     * Panel representing the recent diffs tab of the application.
     */
    private JPanel recentDiffsTab;

    /**
     * Button to trigger a new scan operation.
     */
    private JButton scanButton;

    /**
     * Table displaying differences found during scans.
     */
    private JTable diffTable;

    /**
     * Button for adding a new directory to be monitored.
     */
    private JButton newDirectoryButton;

    /**
     * Table showing results of recent scans.
     */
    private JTable recentScanTable;

    /**
     * Table listing all currently monitored directories.
     */
    private JTable monitoredDirectoriesTable;

    /**
     * Progress bar for displaying scan progress.
     */
    private JProgressBar scanProgress;

    /**
     * Container panel that holds the scan progress bar and related elements.
     */
    private JPanel scanProgressContainer;

    /**
     * Label displaying text about the current status of the scan progress.
     */
    private JLabel scanProgressLabel;

    /**
     * Text pane for displaying live feed information.
     */
    private JTextPane liveFeedText;

    /**
     * Title label for the live feed section.
     */
    private JLabel liveFeedTitle;

    /**
     * Scroll pane container for the live feed text pane.
     */
    private JScrollPane liveFeedContainer;

    /**
     * Label displaying the count of diffs in the live feed.
     */
    private JLabel liveFeedDiffCount;

    /**
     * Panel representing the settings tab of the application.
     */
    private JPanel settingsTab;

    /**
     * Text pane for displaying details about differences (diffs).
     */
    private JTextPane diffDetails;

    /**
     * Button to clear the live feed text.
     */
    private JButton clearFeedButton;

    /**
     * Panel representing the files tab of the application.
     */
    private JPanel filesTab;

    /**
     * Table for displaying files.
     */
    private JTable filesTable;

    /**
     * Search field for searching through files.
     */
    private JTextField fileSearchField;

    /**
     * Checkbox to include containing directories in search results.
     */
    private JCheckBox searchContainingCheckBox;

    /**
     * Checkbox to sort search results in descending order.
     */
    private JCheckBox descendingCheckBox;

    /**
     * Table for displaying a summary of file scans.
     */
    private JTable fileSummaryTable;

    /**
     * Text pane for displaying details about scan summaries.
     */
    private JTextPane scanSummaryDetails;

    /**
     * Label to display the count of search results.
     */
    private JLabel searchResultCount;

    /**
     * Combo box for selecting application themes.
     */
    private JComboBox<AppTheme> themePicker;

    /**
     * Button to restart an operation or the application.
     */
    private JButton restartButton;

    /**
     * List displaying monitored directories.
     */
    private JList<MonitoredDirectory> monitoredDirectoryList;

    /**
     * Panel for displaying notifications.
     */
    private JPanel notificationPanel;

    /**
     * Text area for displaying detailed notification messages.
     */
    private JTextArea notificationTextArea;

    /**
     * Table for displaying quarantined items.
     */
    private JTable quarantineTable;

    /**
     * Checkbox to show or hide the splash screen on application startup.
     */
    private JCheckBox showSplashScreenCheckBox;

    /**
     * Slider for setting the maximum file size for hash extraction.
     */
    private JSlider maxHashExtractionFileSizeSlider;

    /**
     * Label displaying the value of the maximum file size for hash extraction.
     */
    private JLabel maxHashExtractionFileSizeValueLabel;

    /**
     * Checkbox to set the maximum hash extraction file size to unlimited.
     */
    private JCheckBox maxHashExtractionFileSizeUnlimitedCheckbox;

    @Override
    public String getScreenName() {
        return "Home";
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Returns the button used to add a new directory.
     *
     * @return JButton representing the "Add New Directory" button.
     */
    public JButton getAddNewDirectoryButton() {
        return newDirectoryButton;
    }

    /**
     * Returns the scan button.
     *
     * @return JButton representing the "Scan" button.
     */
    public JButton getScanButton() {
        return scanButton;
    }

    /**
     * Returns the recent scans table.
     *
     * @return JTable displaying results of recent scans.
     */
    public JTable getRecentScanTable() {
        return recentScanTable;
    }

    /**
     * Returns the monitored directories table.
     *
     * @return JTable listing all currently monitored directories.
     */
    public JTable getMonitoredDirectoriesTable() {
        return monitoredDirectoriesTable;
    }

    /**
     * Returns the diffs table.
     *
     * @return JTable displaying differences found during scans.
     */
    public JTable getDiffTable() {
        return diffTable;
    }

    /**
     * Returns the container panel for scan progress elements.
     *
     * @return JPanel containing the scan progress bar and label.
     */
    public JPanel getScanProgressContainer() {
        return scanProgressContainer;
    }

    /**
     * Returns the scroll pane container for live feed text.
     *
     * @return JScrollPane that contains the live feed JTextPane.
     */
    public JScrollPane getLiveFeedContainer() {
        return liveFeedContainer;
    }

    /**
     * Returns the live feed text pane.
     *
     * @return JTextPane displaying live feed information.
     */
    public JTextPane getLiveFeedText() {
        return liveFeedText;
    }

    /**
     * Returns the title label for the live feed section.
     *
     * @return JLabel representing the live feed title.
     */
    public JLabel getLiveFeedTitle() {
        return liveFeedTitle;
    }

    /**
     * Returns the label displaying the count of diffs in the live feed.
     *
     * @return JLabel showing the live feed diff count.
     */
    public JLabel getLiveFeedDiffCount() {
        return liveFeedDiffCount;
    }

    /**
     * Returns the tabbed pane component.
     *
     * @return JTabbedPane allowing switching between different tabs.
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Returns the text pane for displaying diff details.
     *
     * @return JTextPane showing details about differences (diffs).
     */
    public JTextPane getDiffDetails() {
        return diffDetails;
    }

    /**
     * Returns the button used to clear the live feed.
     *
     * @return JButton representing the "Clear Feed" button.
     */
    public JButton getClearFeedButton() {
        return clearFeedButton;
    }

    /**
     * Returns the file search field.
     *
     * @return JTextField for searching through files.
     */
    public JTextField getFileSearchField() {
        return fileSearchField;
    }

    /**
     * Returns the table displaying a summary of file scans.
     *
     * @return JTable showing file scan summaries.
     */
    public JTable getFileScanSummaryTable() {
        return fileSummaryTable;
    }

    /**
     * Returns the checkbox for sorting search results in descending order.
     *
     * @return JCheckBox representing the "Descending" option.
     */
    public JCheckBox getDescendingCheckBox() {
        return descendingCheckBox;
    }

    /**
     * Returns the table displaying files.
     *
     * @return JTable listing files.
     */
    public JTable getFilesTable() {
        return filesTable;
    }

    /**
     * Returns the checkbox for including containing directories in search results.
     *
     * @return JCheckBox representing the "Search Containing" option.
     */
    public JCheckBox getSearchContainingCheckBox() {
        return searchContainingCheckBox;
    }

    /**
     * Returns the text pane for displaying scan summary details.
     *
     * @return JTextPane showing details about scan summaries.
     */
    public JTextPane getScanSummaryDetails() {
        return scanSummaryDetails;
    }

    /**
     * Returns the label displaying the count of search results.
     *
     * @return JLabel representing the search result count.
     */
    public JLabel getSearchResultCount() {
        return searchResultCount;
    }

    /**
     * Returns the combo box for selecting application themes.
     *
     * @return JComboBox containing AppTheme options.
     */
    public JComboBox<AppTheme> getThemePicker() {
        return themePicker;
    }

    /**
     * Returns the checkbox to show or hide the splash screen on startup.
     *
     * @return JCheckBox representing the "Show Splash Screen" option.
     */
    public JCheckBox getShowSplashScreenCheckBox() {
        return showSplashScreenCheckBox;
    }

    /**
     * Returns the button used to restart an operation or the application.
     *
     * @return JButton representing the "Restart" button.
     */
    public JButton getRestartButton() {
        return restartButton;
    }

    /**
     * Returns the list displaying monitored directories.
     *
     * @return JList containing MonitoredDirectory objects.
     */
    public JList<MonitoredDirectory> getMonitoredDirectoryList() {
        return monitoredDirectoryList;
    }

    /**
     * Returns the panel for displaying notifications.
     *
     * @return JPanel showing notification messages.
     */
    public JPanel getNotificationPanel() {
        return notificationPanel;
    }

    /**
     * Returns the text area for displaying detailed notification messages.
     *
     * @return JTextArea containing notification details.
     */
    public JTextArea getNotificationTextArea() {
        return notificationTextArea;
    }

    /**
     * Returns the table displaying quarantined items.
     *
     * @return JTable listing quarantined items.
     */
    public JTable getQuarantineTable() {
        return quarantineTable;
    }

    /**
     * Returns the slider for setting the maximum file size for hash extraction.
     *
     * @return JSlider for maximum hash extraction file size.
     */
    public JLabel getMaxHashExtractionFileSizeValueLabel() {
        return maxHashExtractionFileSizeValueLabel;
    }

    /**
     * Returns the slider for setting the maximum file size for hash extraction.
     *
     * @return JSlider for maximum hash extraction file size.
     */
    public JSlider getMaxHashExtractionFileSizeSlider() {
        return maxHashExtractionFileSizeSlider;
    }

    /**
     * Returns the checkbox to set the maximum hash extraction file size to
     * unlimited.
     *
     * @return JCheckBox representing the "Unlimited" option for max hash extraction
     *         file size.
     */
    public JCheckBox getMaxHashExtractionFileSizeUnlimitedCheckbox() {
        return maxHashExtractionFileSizeUnlimitedCheckbox;
    }

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane = new JTabbedPane();
        Font tabbedPaneFont = this.$$$getFont$$$(null, -1, 14, tabbedPane.getFont());
        if (tabbedPaneFont != null)
            tabbedPane.setFont(tabbedPaneFont);
        tabbedPane.setTabPlacement(1);
        tabbedPane.setToolTipText("");
        rootPanel.add(tabbedPane,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        homeTab = new JPanel();
        homeTab.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("\uD83C\uDFE0 Home", homeTab);
        newDirectoryButton = new JButton();
        newDirectoryButton.setText("New directory");
        homeTab.add(newDirectoryButton,
                new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        homeTab.add(splitPane1,
                new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel1);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 16, label1.getFont());
        if (label1Font != null)
            label1.setFont(label1Font);
        label1.setText("Most recent scans");
        panel1.add(label1,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setToolTipText("Double click a scan to see details");
        panel1.add(scrollPane1,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null,
                        0, false));
        recentScanTable = new JTable();
        recentScanTable.setAutoResizeMode(2);
        recentScanTable.setShowHorizontalLines(true);
        scrollPane1.setViewportView(recentScanTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel2);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 16, label2.getFont());
        if (label2Font != null)
            label2.setFont(label2Font);
        label2.setText("Monitored directories");
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null,
                        0, false));
        monitoredDirectoryList = new JList<MonitoredDirectory>();
        monitoredDirectoryList.setEnabled(true);
        monitoredDirectoryList.setSelectionMode(0);
        monitoredDirectoryList.setToolTipText("Monitored directories list");
        scrollPane2.setViewportView(monitoredDirectoryList);
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 10, 0), -1, -1));
        homeTab.add(notificationPanel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, true));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 16, label3.getFont());
        if (label3Font != null)
            label3.setFont(label3Font);
        label3.setText("Notifications");
        notificationPanel.add(label3,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        notificationTextArea = new JTextArea();
        notificationTextArea.setEditable(false);
        notificationPanel.add(notificationTextArea,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null,
                        new Dimension(150, 50), null, 0, false));
        scanTab = new JPanel();
        scanTab.setLayout(new GridLayoutManager(10, 4, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("\uD83D\uDD0E Scan", scanTab);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 16, label4.getFont());
        if (label4Font != null)
            label4.setFont(label4Font);
        label4.setHorizontalAlignment(0);
        label4.setText("Monitored directories");
        scanTab.add(label4,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setToolTipText("Right click a monitored directory for different actions");
        scanTab.add(scrollPane3,
                new GridConstraints(1, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null,
                        0, false));
        monitoredDirectoriesTable = new JTable();
        monitoredDirectoriesTable.setAutoResizeMode(2);
        scrollPane3.setViewportView(monitoredDirectoriesTable);
        liveFeedContainer = new JScrollPane();
        scanTab.add(liveFeedContainer,
                new GridConstraints(1, 1, 8, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        new Dimension(450, -1), new Dimension(450, -1), null, 0, false));
        liveFeedText = new JTextPane();
        liveFeedText.setEditable(false);
        liveFeedText.setText("");
        liveFeedContainer.setViewportView(liveFeedText);
        liveFeedTitle = new JLabel();
        Font liveFeedTitleFont = this.$$$getFont$$$(null, Font.BOLD, 16, liveFeedTitle.getFont());
        if (liveFeedTitleFont != null)
            liveFeedTitle.setFont(liveFeedTitleFont);
        liveFeedTitle.setHorizontalAlignment(0);
        liveFeedTitle.setText("Scan logs");
        scanTab.add(liveFeedTitle,
                new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        liveFeedDiffCount = new JLabel();
        liveFeedDiffCount.setText("");
        scanTab.add(liveFeedDiffCount,
                new GridConstraints(9, 2, 1, 2, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        scanButton = new JButton();
        scanButton.setText("Full scan");
        scanTab.add(scanButton,
                new GridConstraints(6, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearFeedButton = new JButton();
        clearFeedButton.setText("Clear feed");
        scanTab.add(clearFeedButton,
                new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filesTab = new JPanel();
        filesTab.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("📁 Files", filesTab);
        final JSplitPane splitPane2 = new JSplitPane();
        filesTab.add(splitPane2,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        splitPane2.setLeftComponent(scrollPane4);
        filesTable = new JTable();
        scrollPane4.setViewportView(filesTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane2.setRightComponent(panel3);
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setOrientation(0);
        panel3.add(splitPane3,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        splitPane3.setLeftComponent(scrollPane5);
        fileSummaryTable = new JTable();
        scrollPane5.setViewportView(fileSummaryTable);
        final JScrollPane scrollPane6 = new JScrollPane();
        splitPane3.setRightComponent(scrollPane6);
        scanSummaryDetails = new JTextPane();
        scanSummaryDetails.setEditable(false);
        scrollPane6.setViewportView(scanSummaryDetails);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        filesTab.add(panel4,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, true));
        fileSearchField = new JTextField();
        panel4.add(fileSearchField,
                new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Search for file");
        panel4.add(label5, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchContainingCheckBox = new JCheckBox();
        searchContainingCheckBox.setText("File name contains");
        panel4.add(searchContainingCheckBox,
                new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        descendingCheckBox = new JCheckBox();
        descendingCheckBox.setText("Descending");
        panel4.add(descendingCheckBox,
                new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchResultCount = new JLabel();
        searchResultCount.setText("");
        panel4.add(searchResultCount,
                new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        recentDiffsTab = new JPanel();
        recentDiffsTab.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("⚠\uFE0F Recent diffs", recentDiffsTab);
        final JSplitPane splitPane4 = new JSplitPane();
        recentDiffsTab.add(splitPane4,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane7 = new JScrollPane();
        splitPane4.setLeftComponent(scrollPane7);
        diffTable = new JTable();
        diffTable.setAutoResizeMode(4);
        diffTable.setFillsViewportHeight(true);
        scrollPane7.setViewportView(diffTable);
        final JScrollPane scrollPane8 = new JScrollPane();
        splitPane4.setRightComponent(scrollPane8);
        diffDetails = new JTextPane();
        scrollPane8.setViewportView(diffDetails);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, Font.BOLD, 16, label6.getFont());
        if (label6Font != null)
            label6.setFont(label6Font);
        label6.setText("Most recently detected diffs");
        recentDiffsTab.add(label6,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        settingsTab = new JPanel();
        settingsTab.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane.addTab("⚙\uFE0F Settings", settingsTab);
        final JSplitPane splitPane5 = new JSplitPane();
        settingsTab.add(splitPane5,
                new GridConstraints(0, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setLeftComponent(panel5);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, Font.BOLD, 14, label7.getFont());
        if (label7Font != null)
            label7.setFont(label7Font);
        label7.setText("Settings");
        panel5.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        themePicker = new JComboBox<AppTheme>();
        panel5.add(themePicker,
                new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        restartButton = new JButton();
        restartButton.setText("Restart");
        panel5.add(restartButton,
                new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("© 2025 PWSS ORG");
        panel5.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6,
                new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        showSplashScreenCheckBox = new JCheckBox();
        showSplashScreenCheckBox.setText("Show splash screen");
        panel6.add(showSplashScreenCheckBox,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxHashExtractionFileSizeSlider = new JSlider();
        maxHashExtractionFileSizeSlider.setMajorTickSpacing(5120);
        maxHashExtractionFileSizeSlider.setMaximum(102400);
        maxHashExtractionFileSizeSlider.setMinimum(100);
        maxHashExtractionFileSizeSlider.setMinorTickSpacing(1024);
        maxHashExtractionFileSizeSlider.setPaintLabels(false);
        maxHashExtractionFileSizeSlider.setPaintTicks(true);
        maxHashExtractionFileSizeSlider.setPaintTrack(true);
        maxHashExtractionFileSizeSlider.setSnapToTicks(true);
        panel6.add(maxHashExtractionFileSizeSlider,
                new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        final JLabel label9 = new JLabel();
        label9.setText("Max hash extraction file size (MB)");
        panel6.add(label9, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel6.add(separator1,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0,
                        false));
        maxHashExtractionFileSizeValueLabel = new JLabel();
        maxHashExtractionFileSizeValueLabel.setText("Selected:");
        panel6.add(maxHashExtractionFileSizeValueLabel,
                new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        maxHashExtractionFileSizeUnlimitedCheckbox = new JCheckBox();
        maxHashExtractionFileSizeUnlimitedCheckbox.setText("Unlimited");
        panel6.add(maxHashExtractionFileSizeUnlimitedCheckbox,
                new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Theme");
        panel5.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane5.setRightComponent(panel7);
        final JScrollPane scrollPane9 = new JScrollPane();
        panel7.add(scrollPane9,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        quarantineTable = new JTable();
        scrollPane9.setViewportView(quarantineTable);
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, Font.BOLD, 14, label11.getFont());
        if (label11Font != null)
            label11.setFont(label11Font);
        label11.setText("Quarantined files");
        panel7.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scanProgressContainer = new JPanel();
        scanProgressContainer.setLayout(new GridLayoutManager(1, 2, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.add(scanProgressContainer,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, true));
        scanProgress = new JProgressBar();
        scanProgress.setIndeterminate(true);
        scanProgressContainer.add(scanProgress,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        scanProgressLabel = new JLabel();
        scanProgressLabel.setText("Scan in progress");
        scanProgressContainer.add(scanProgressLabel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null)
            return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(),
                size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize())
                : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
