package dbdiff.pojos.db;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DB schema item that contains or references a set of columns.
 */
public abstract class BaseColumnContainer extends NamedSchemaItem {
    private final Map<String, Column> columnsByName = new LinkedHashMap<>();

    public BaseColumnContainer(final String catalog, final String schema, final String name) {
        super(catalog, schema, name);
    }
    public BaseColumnContainer(final String catalog, final String schema, final String name, final List<Column> columns) {
        super(catalog, schema, name);
        setColumns(columns);
    }

    public Collection<Column> getColumns() {
        return columnsByName.values();
    }

    public void setColumns(final List<Column> columns) {
        for (final var column : columns) {
            columnsByName.put(column.name, column);
        }
    }

    public List<String> getColumnNames() {
        List<String> names = Lists.newArrayList();
        for (Column col : columnsByName.values()) {
            names.add(col.name);
        }
        return names;
    }

    public Column getColumnByName(String name) {
        return columnsByName.get(name);
    }
}