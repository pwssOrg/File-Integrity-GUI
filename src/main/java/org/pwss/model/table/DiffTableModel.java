package org.pwss.model.table;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.metadata.MetadataManager;
import org.pwss.model.entity.Diff;

/**
 * The DiffTableModel is a model for a table that displays differences (diffs) between file states.
 * It extends AbstractTableModel and provides data to the JTable component in a Swing application.
 */
public class DiffTableModel extends AbstractTableModel {
    private final List<Diff> data;
    private final MetadataManager metadataManager;
    public static final String[] columns = {"\uD83D\uDDCE File Path", "\uD83D\uDD8A️ Modified", "⚠️ Detected", "👮 Quarantine"};

    /**
     * Constructs a new DiffTableModel with the specified list of diffs.
     *
     * @param data the list of Diff objects to be displayed in the table
     */
    public DiffTableModel(List<Diff> data) {
        this.data = data;
        this.metadataManager = new MetadataManager();
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
            case 1, 2 -> Date.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Diff diff = data.get(rowIndex);
        final long fileId = diff.integrityFail().file().id();

        return switch (columnIndex) {
            case 0 -> diff.baseline().file().path();
            case 1 -> diff.integrityFail().file().mtime();
            case 2 -> diff.time().created();
            // Column 3 (Quarantine) - show ❌ if quarantined, otherwise 📥
            case 3 -> metadataManager.isFileQuarantined(fileId) ? "❌" : "\uD83D\uDCE5";
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Only column 3 is ever editable (Quarantine column)
        if (columnIndex != 3) {
            return false;
        }

        // Get the diff safely and set cell editable only if the file is not already quarantined
        return getDiffAt(rowIndex)
                .map(Diff::integrityFail)
                .map(fail -> !metadataManager.isFileQuarantined(fail.file().id()))
                .orElse(true);
    }

    /**
     * Get the Diff object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the Diff object at the specified row index,
     * or an empty Optional if the index is out of bounds.
     */
    public Optional<Diff> getDiffAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
