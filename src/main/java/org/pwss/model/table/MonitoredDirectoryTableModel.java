package org.pwss.model.table;

import org.pwss.model.entity.MonitoredDirectory;
import org.pwss.model.entity.Time;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;

public class MonitoredDirectoryTableModel extends AbstractTableModel {
    private final List<MonitoredDirectory> directories;
    private final String[] columnNames = {
            "ID", "Path", "Active", "Added At", "Last Scanned",
            "Notes", "Baseline Established", "Include Subdirectories"
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
            case 0 -> Long.class;
            case 2, 6, 7 -> Boolean.class;
            case 3 -> Time.class;
            case 4 -> Date.class;
            default -> String.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MonitoredDirectory dir = directories.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> dir.id();
            case 1 -> dir.path();
            case 2 -> dir.isActive();
            case 3 -> dir.addedAt().created();
            case 4 -> dir.lastScanned();
            case 5 -> dir.notes() != null ? dir.notes().notes() : "";
            case 6 -> dir.baselineEstablished();
            case 7 -> dir.includeSubdirectories();
            default -> null;
        };
    }
}

