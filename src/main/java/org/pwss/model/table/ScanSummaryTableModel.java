package org.pwss.model.table;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.model.entity.ScanSummary;

public class ScanSummaryTableModel extends AbstractTableModel {
    private final List<ScanSummary> data;
    private final String[] columns = {"Scan id", "Scan time", "Notes", "Scan completed"};

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
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 1 -> Date.class;
            case 2 -> String.class;
            case 3 -> Boolean.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ScanSummary summary = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> summary.scan().id();
            case 1 -> summary.scan().scanTime().created();
            case 2 -> summary.scan().notes().notes();
            case 3 -> summary.scan().status().equals("COMPLETED");
            default -> null;
        };
    }

    /**
     * Get the ScanSummary object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the ScanSummary object at the specified row index,
     * or an empty Optional if the index is out of bounds.
     */
    public Optional<ScanSummary> getSummaryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
