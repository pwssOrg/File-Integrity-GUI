package org.pwss.model.table;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.model.entity.MonitoredDirectory;

public class MonitoredDirectoryTableModel extends AbstractTableModel {
    private final List<MonitoredDirectory> directories;
    private final String[] columnNames = {
            "\uD83D\uDCC1 Path", "\uD83D\uDD0E Active", "\uD83D\uDD59 Last Scanned", "\uD83D\uDEE1️ Baseline Established", "\uD83D\uDDC2️ Include Subdirectories"
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
            case 2 -> Date.class;
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
     * Get the MonitoredDirectory object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the MonitoredDirectory object at the specified row index,
     * or an empty Optional if the index is out of bounds.
     */
    public Optional<MonitoredDirectory> getDirectoryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < directories.size()) {
            return Optional.of(directories.get(rowIndex));
        }
        return Optional.empty();
    }
}

