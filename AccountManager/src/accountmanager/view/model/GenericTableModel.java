package accountmanager.view.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import accountmanager.entity.PersistentEntity;

/**
 *
 * @author doobs
 * @param <T> type of the entity
 */
public class GenericTableModel<T extends PersistentEntity> extends AbstractTableModel {

    private final List<T> items;
    private final String COLUMN_NAMES[];

    public GenericTableModel(final String[] columnNames) {
        this.items = new ArrayList<>();
        this.COLUMN_NAMES = columnNames;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return items.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        items.get(rowIndex).set(columnIndex, aValue);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0) {
            return getValueAt(0, columnIndex).getClass();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public void addEntity(T item, boolean isInit) {
        items.add(item);
        int row = items.size() - 1;
        fireTableRowsInserted(row, row);
    }

    public void removeEntity(int id) {
        items.removeIf(item -> item.getId().equals(id));
        fireTableRowsDeleted(id, id);
    }

    public void updateEntity(T entity) {
        items.replaceAll((T elem) -> {
            Integer id = elem.getId();
            if (id.equals(entity.getId())) {
                fireTableRowsUpdated(0, id);
                return entity;
            }
            return elem;
        });
    }

}
