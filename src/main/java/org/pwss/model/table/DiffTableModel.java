package org.pwss.model.table;

import org.pwss.model.entity.Diff;
import org.pwss.model.entity.ScanSummary;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DiffTableModel extends AbstractTableModel {
    private final List<Diff> data;
    private final String[] columns = {"\uD83D\uDDCE File Path", "\uD83D\uDD8A️ Modified", "⚠️ Detected"};

    public DiffTableModel(List<Diff> data) {
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
        Diff diff = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> diff.baseline().file().path();
            case 1 -> diff.integrityFail().file().mtime();
            case 2 -> diff.time().created();
            default -> null;
        };
    }

    /**
     * Get the Diff object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the Diff object at the specified row index,
     *         or an empty Optional if the index is out of bounds.
     */
    public Optional<Diff> getDiffAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
