package dbdiff.pojos.relationalDb;

import com.google.common.collect.Lists;
import dbdiff.pojos.db.CatalogSchema;
import dbdiff.pojos.db.Column;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DB schema item that contains or references a set of columns.
 */
public abstract class BaseColumnContainer extends NamedSchemaItem {
    private final Map<String, Column> m_columnsByName = new LinkedHashMap<>(); //An internal search index of cols by name.

    /**
     * Create a new instance.
     * @param catalogSchema catalog/schema.
     * @param name name.
     */
    public BaseColumnContainer(CatalogSchema catalogSchema, String name) {
        super(catalogSchema, name);
    }

    /**
     * Create a new instance.
     * @param catalog catalog.
     * @param schema schema.
     * @param name name.
     */
    public BaseColumnContainer(String catalog, String schema, String name) {
        super(catalog, schema, name);
    }

    /**
     * @return ordered collection of columns.
     */
    public Collection<Column> getColumns() {
        return m_columnsByName.values();
    }

    /**
     * Set the columns.
     * @param columns The columns to set
     */
    public void setColumns(List<Column> columns) {
        for (Column c : columns) {
            m_columnsByName.put(c.getName(), c);
        }
    }

    /**
     * @return ordered collection of column names.
     */
    public List<String> getColumnNames() {
        List<String> names = Lists.newArrayList();
        for (Column col : m_columnsByName.values()) {
            names.add(col.getName());
        }
        return names;
    }

    /**
     * Get a column by name.
     * @param name name.
     * @return column with the given name (or null).
     */
    public Column getColumnByName(String name) {
        return m_columnsByName.get(name);
    }
}