package org.pwss.model.table;

import org.pwss.model.entity.MonitoredDirectory;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MonitoredDirectoryTableModel extends AbstractTableModel {
    private final List<MonitoredDirectory> directories;
    private final String[] columnNames = {
            "Path", "Active", "Last Scanned", "Baseline Established", "Include Subdirectories"
    };

    public MonitoredDirectoryTableModel(List<MonitoredDirectory> directories) {
        this.directories = directories;
    }

    @Override
    public int getRowCount() {
        return directories.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 1, 4 -> Boolean.class;
            default -> super.getColumnClass(columnIndex);
        };
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MonitoredDirectory dir = directories.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> dir.path();
            case 1 -> dir.isActive();
            case 2 -> dir.lastScanned();
            case 3 -> dir.baselineEstablished() ? "Yes" : "No";
            case 4 -> dir.includeSubdirectories();
            default -> null;
        };
    }

    /**
     * Returns the MonitoredDirectory object at the specified row index.
     *
     * @param rowIndex the index of the row
     * @return the MonitoredDirectory object at the specified row, or null if the index is out of bounds
     */
    public MonitoredDirectory getDirectoryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < directories.size()) {
            return directories.get(rowIndex);
        }
        return null;
    }
}

