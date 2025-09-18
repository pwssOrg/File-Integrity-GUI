package org.pwss.model.table;

import org.pwss.model.entity.ScanSummary;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Optional;

public class ScanSummaryTableModel extends AbstractTableModel {
    private final List<ScanSummary> data;
    private final String[] columns = {"File Name"};

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
            case 0 -> summary.file().basename();
            default -> null;
        };
    }

    /**
     * Get the ScanSummary object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the ScanSummary object at the specified row index,
     *         or an empty Optional if the index is out of bounds.
     */
    public Optional<ScanSummary> getSummaryAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
