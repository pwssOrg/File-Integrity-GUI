package org.pwss.model.table;

import org.pwss.model.entity.ScanSummary;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ScanSummaryTableModel extends AbstractTableModel {
    private final List<ScanSummary> data;
    private final String[] columns = {"File Path", "Last modified", "Notes"};

    public ScanSummaryTableModel(List<ScanSummary> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ScanSummary summary = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> summary.file().path();
            case 1 -> summary.file().mtime();
            case 2 -> summary.scan().notes().notes();
            default -> null;
        };
    }

    /**
     * Get the ScanSummary object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return the ScanSummary object at the specified row index, or null if the index is out of bounds.
     */
    public ScanSummary getSummaryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return data.get(rowIndex);
        }
        return null;
    }
}
