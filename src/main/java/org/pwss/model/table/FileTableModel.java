package org.pwss.model.table;

import java.util.List;
import java.util.Optional;
import javax.swing.table.AbstractTableModel;
import org.pwss.model.entity.File;

public class FileTableModel extends AbstractTableModel {
    private final List<File> data;
    private final String[] columns = {"\uD83D\uDEA6 Basename", "Directory"};

    public FileTableModel(List<File> data) {
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
        File file = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> file.basename();
            case 1 -> file.directory();
            default -> null;
        };
    }

    /**
     * Retrieves the File object at the specified row index.
     *
     * @param rowIndex The index of the row for which to retrieve the File object.
     * @return An Optional containing the File object if the index is valid, or an empty Optional if the index is out of bounds.
     */
    public Optional<File> getFileAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return Optional.of(data.get(rowIndex));
        }
        return Optional.empty();
    }
}
