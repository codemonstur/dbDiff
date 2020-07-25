package dbdiff.pojos.db;

import java.util.Objects;

/**
 * This class defines the data type of a database column.  It contains integer and String descriptions of a type.
 */
public final class ColumnType {

    // an integer that defines a data type, the sql description
    public final int typeId;
    // a String that describes a data type, the Java description
    public final String typeCode;

    public ColumnType(final int typeId, final String typeCode) {
        this.typeId = typeId;
        this.typeCode = typeCode;
    }

    @Override
    public boolean equals(Object t) {
        if (t == null || getClass() != t.getClass()) return false;

        final ColumnType other = (ColumnType) t;
        return typeId == other.typeId
            && Objects.equals(typeCode, other.typeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, typeCode);
    }
}
