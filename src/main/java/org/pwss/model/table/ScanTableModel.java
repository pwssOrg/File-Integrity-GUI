package org.pwss.model.table;

import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.model.entity.Scan;

public class ScanTableModel extends AbstractTableModel {
    private final List<Scan> scans;
    private final String[] columnNames = {
            "\uD83D\uDCC1 Directory", "\uD83D\uDD59 Scan Time", "\uD83D\uDEA6 Status"
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
            case 0 -> scan.monitoredDirectory().path();
            case 1 -> scan.scanTime().created();
            case 2 -> scan.status();
            default -> null;
        };
    }

    /**
     * Returns the Scan object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return the Scan object at the specified row index, or null if the index is out of bounds.
     */
    public Optional<Scan> getScanAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < scans.size()) {
            return Optional.of(scans.get(rowIndex));
        }
        return Optional.empty();
    }
}
