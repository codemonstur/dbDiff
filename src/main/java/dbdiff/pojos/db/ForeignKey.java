package dbdiff.pojos.db;

import com.google.common.base.Objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ForeignKey {

    public final String fkName;
    public final String keySeq;

    public final CatalogAndSchema fkCatalogAndSchema;
    public final String fkTable;
    public final String fkColumn;

    public final CatalogAndSchema pkCatalogAndSchema;
    public final String pkTable;
    public final String pkColumn;

    public ForeignKey(final ResultSet fkResultSet) throws SQLException {
        this.fkName = fkResultSet.getString(12);
        this.keySeq = fkResultSet.getString(9);
        this.fkCatalogAndSchema = new CatalogAndSchema(fkResultSet.getString(5), fkResultSet.getString(6));
        this.fkTable = fkResultSet.getString(7);
        this.fkColumn = fkResultSet.getString(8);
        this.pkCatalogAndSchema = new CatalogAndSchema(fkResultSet.getString(1), fkResultSet.getString(2));
        this.pkTable = fkResultSet.getString(3);
        this.pkColumn = fkResultSet.getString(4);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(37, pkCatalogAndSchema, pkTable, pkColumn,
                fkCatalogAndSchema, fkTable, fkColumn,
                fkName, keySeq);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ForeignKey)) return false;

        final ForeignKey other = (ForeignKey) o;
        return equalsReference(other)
            && equalsFrom(other)
            && Objects.equal(fkName, other.fkName)
            && Objects.equal(keySeq, other.keySeq);
    }

    /**
     * Returns true if this foreign key refers to the same table/column as another foreign key
     * (ie the referenced catalog, schema, table, and column of the two fk's are equal)
     * @param other Another fk to check
     * @return True if the two fk's point to the same column
     */
    public boolean equalsReference(ForeignKey other) {
        return Objects.equal(pkCatalogAndSchema, other.pkCatalogAndSchema)
            && Objects.equal(pkTable, other.pkTable)
            && Objects.equal(pkColumn, other.pkColumn);
    }

    /**
     * Returns true if this foreign key is based off of the same table/column as another foreign key
     * (ie the catalog, schema, table, and column that define the fk reference are equal)
     * @param other Another fk to check
     * @return True if the two fk's point from the same column
     */
    public boolean equalsFrom(ForeignKey other) {
        return Objects.equal(fkCatalogAndSchema, other.fkCatalogAndSchema)
            && Objects.equal(fkTable, other.fkTable)
            && Objects.equal(fkColumn, other.fkColumn);
    }

    @Override
    public String toString() {
        return fkName + "(" + keySeq + "): " + fkTable + "(" + fkColumn + ")-->" + pkTable + "(" + pkColumn + ")";
    }
}
