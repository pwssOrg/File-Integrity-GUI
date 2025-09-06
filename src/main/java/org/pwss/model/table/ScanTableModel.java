package org.pwss.model.table;

import org.pwss.model.entity.Scan;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ScanTableModel extends AbstractTableModel {
    private final List<Scan> scans;
    private final String[] columnNames = {
            "ID", "Scan Time", "Status", "Notes", "Directory", "Baseline"
    };

    public ScanTableModel(List<Scan> scans) {
        this.scans = scans;
    }

    @Override
    public int getRowCount() {
        return scans.size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Scan scan = scans.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> scan.id();
            case 1 -> scan.scanTime().created();
            case 2 -> scan.status();
            case 3 -> scan.notes() != null ? scan.notes().notes() : "";
            case 4 -> scan.monitoredDirectory() != null ? scan.monitoredDirectory().path() : "";
            case 5 -> scan.isBaselineScan();
            default -> null;
        };
    }
}
