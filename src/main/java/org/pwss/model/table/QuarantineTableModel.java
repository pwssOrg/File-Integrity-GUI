package org.pwss.model.table;

import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.model.entity.QuarantineMetadata;

/**
 * Table model for displaying quarantine metadata in a table.
 */
public class QuarantineTableModel extends AbstractTableModel {
    private final List<QuarantineMetadata> data;
    public static final String[] columns = {"File id", "File path",};

    /**
     * Constructs a QuarantineTableModel with the specified data.
     * @param data the list of QuarantineMetadata to be displayed in the table.
     */
    public QuarantineTableModel(List<QuarantineMetadata> data) {
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
        QuarantineMetadata metadata = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> metadata.fileId();
            case 1 -> metadata.keyName();
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }

    /**
     * Get the QuarantineMetadata object at the specified row index.
     *
     * @param rowIndex the index of the row in the table.
     * @return an Optional containing the QuarantineMetadata object at the specified row index,
     * or an empty Optional if the index is out of bounds.
     */
    public Optional<QuarantineMetadata> getMetadataAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
