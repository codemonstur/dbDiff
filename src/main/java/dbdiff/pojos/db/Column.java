package dbdiff.pojos.db;

import dbdiff.pojos.relationalDb.NamedSchemaItem;

import java.util.Objects;

/**
 * A model of a DB Table's column
 */
public class Column extends NamedSchemaItem implements Comparable<Column> {
    private final String m_table;

    // Column properties
    private String m_default;
    private Boolean m_isNullable;
    private Integer m_columnSize;
    private Integer m_ordinal;
    private ColumnType m_columnType;

    /**
     * Construct a new Column.
     * @param catalogSchema catalog/schema.
     * @param name column name.
     * @param table name of the table that contains the column.
     */
    public Column(CatalogSchema catalogSchema, String name, String table) {
        super(catalogSchema, name);
        m_table = table;
    }

    /**
     * Create a new Column.
     * @param catalog catalog.
     * @param schema schema.
     * @param name column name.
     * @param table name of the table that contains the column.
     */
    public Column(String catalog, String schema, String name, String table) {
        super(catalog, schema, name);
        m_table = table;
    }

    /**
     * Get the table.
     * @return Returns the table
     */
    public String getTable() {
        return m_table;
    }

    /**
     * Get the default.
     * @return Returns the default
     */
    public String getDefault() {
        return m_default;
    }

    /**
     * Set the default.
     * @param defaultVal The default to set
     */
    public void setDefault(String defaultVal) {
        m_default = defaultVal;
    }

    /**
     * Get the isNullable.
     * @return Returns the isNullable
     */
    public Boolean getIsNullable() {
        return m_isNullable;
    }

    /**
     * Set the isNullable.
     * @param isNullable The isNullable to set
     */
    public void setIsNullable(Boolean isNullable) {
        m_isNullable = isNullable;
    }

    /**
     * Get the size of the column.  For char or date types this is the maximum number of characters, for numeric or decimal types
     * this is precision.
     * @return Returns the columnSize
     */
    public Integer getColumnSize() {
        return m_columnSize;
    }

    /**
     * Set the size of the column.  For char or date types this is the maximum number of characters, for numeric or decimal types
     * this is precision.
     * @param columnSize The columnSize to set
     */
    public void setColumnSize(Integer columnSize) {
        m_columnSize = columnSize;
    }

    /**
     * Get the ordinal.
     * @return Returns the ordinal
     */
    public Integer getOrdinal() {
        return m_ordinal;
    }

    /**
     * Set the ordinal.
     * @param ordinal The ordinal to set
     */
    public void setOrdinal(Integer ordinal) {
        m_ordinal = ordinal;
    }

    /**
     * Get the type, corresponds to java.sql.Types
     * @return Returns the type
     */
    public int getType() {
        return m_columnType.getType();
    }

    /**
     * @return the type name (e.g. float4, bigint, varchar(255))
     */
    public String getTypeName() {
        return m_columnType.getTypeCode();
    }

    /**
     * @return the column type object
     */
    public ColumnType getColumnType() {
        return m_columnType;
    }

    /**
     * set the column type
     * @param columnType the value to set
     */
    public void setColumnType(ColumnType columnType) {
        m_columnType = columnType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Column)) {
            return false;
        }
        Column other = (Column) o;
        return getOrdinal().equals(other.getOrdinal()) && getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_ordinal, getName());
    }

    @Override
    public int compareTo(Column o) {
        int ordComp = getOrdinal().compareTo(o.getOrdinal());
        if (ordComp == 0) {
            return getName().compareTo(o.getName());
        }
        return ordComp;
    }
}
