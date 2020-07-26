package dbdiff.pojos.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.sql.DatabaseMetaData.columnNoNulls;
import static java.sql.DatabaseMetaData.columnNullable;

public final class Column extends NamedSchemaItem implements Comparable<Column> {
    public final String table;

    public final String defaultValue;
    public final Boolean isNullable;
    public final Boolean isAutoIncrement;
    // For char or date types this is the maximum number of characters, for numeric or decimal types
    // this is precision.
    public final Integer columnSize;
    public final Integer ordinal;
    public final ColumnType columnType;

    public Column(final ResultSet set) throws SQLException {
        super(set.getString(1), set.getString(2), set.getString(4));
        this.table = set.getString(3);
        this.columnType = new ColumnType(set.getInt(5), set.getString(6));
        this.columnSize = set.getInt(7);

        final int nullable = set.getInt(11);
        this.isNullable = columnNullable == nullable ? TRUE
                        : columnNoNulls == nullable ? FALSE
                        : null;
        this.isAutoIncrement = "YES".equals(set.getString(23));

        this.defaultValue = set.getString(13);
        this.ordinal = set.getInt(17);
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Column)) return false;

        final Column other = (Column) o;
        return ordinal.equals(other.ordinal) && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinal, name);
    }

    @Override
    public int compareTo(Column o) {
        final int ordComp = ordinal.compareTo(o.ordinal);
        return ordComp == 0 ?  name.compareTo(o.name) :  ordComp;
    }

    public String toSQLType() {
        if (columnType.typeCode.endsWith(" UNSIGNED")) {
            final String typeName = columnType.typeCode.substring(0, columnType.typeCode.lastIndexOf(' '));
            return typeName.toLowerCase() + "("+ columnSize +") unsigned";
        }
        final String collate = columnType.typeCode.startsWith("VARCHAR") ? " COLLATE utf8mb4_bin" : "";
        final int size = columnType.typeCode.startsWith("BIGINT") ? columnSize+1 : columnSize;
        return columnType.typeCode.toLowerCase() + "("+ size +")" + collate;
    }
}
