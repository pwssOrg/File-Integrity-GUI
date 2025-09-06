package org.pwss.view.screen;


import javax.swing.*;

public class HomeScreen extends BaseScreen {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane;
    private JPanel homeTab;
    private JPanel scanTab;
    private JPanel historyTab;
    private JButton quickScanButton;
    private JButton editDirectoryButton;
    private JButton scanButton;
    private JTable historyTable;
    private JButton newDirectoryButton;
    private JTable recentScanTable;
    private JTable monitoredDirectoriesTable;

    @Override
    protected String getScreenName() {
        return "Home";
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JButton getAddNewDirectoryButton() {
        return newDirectoryButton;
    }

    public JButton getEditDirectoryButton() {
        return editDirectoryButton;
    }

    public JButton getScanButton() {
        return scanButton;
    }

    public JButton getQuickScanButton() {
        return quickScanButton;
    }

    public JTable getRecentScanTable() {
        return recentScanTable;
    }

    public JTable getMonitoredDirectoriesTable() {
        return monitoredDirectoriesTable;
    }

    public JTable getHistoryTable() {
        return historyTable;
    }
}
