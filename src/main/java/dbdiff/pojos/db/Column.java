package dbdiff.pojos.db;

import dbdiff.pojos.relationaldb.NamedSchemaItem;

import java.util.Objects;

/**
 * A model of a DB Table's column
 */
public class Column extends NamedSchemaItem implements Comparable<Column> {
    private final String table;

    private String defaultValue;
    private Boolean isNullable;
    // For char or date types this is the maximum number of characters, for numeric or decimal types
    // this is precision.
    private Integer columnSize;
    private Integer ordinal;
    private ColumnType columnType;

    public Column(String catalog, String schema, String name, String table) {
        super(catalog, schema, name);
        this.table = table;
    }

    public String getTable() {
        return table;
    }
    public String getDefault() {
        return defaultValue;
    }
    public void setDefault(String defaultVal) {
        defaultValue = defaultVal;
    }
    public Boolean getIsNullable() {
        return isNullable;
    }
    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
    }
    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }
    public Integer getOrdinal() {
        return ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
    public int getType() {
        return columnType.getType();
    }
    public String getTypeName() {
        return columnType.getTypeCode();
    }
    public ColumnType getColumnType() {
        return columnType;
    }
    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
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
        return Objects.hash(ordinal, getName());
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
