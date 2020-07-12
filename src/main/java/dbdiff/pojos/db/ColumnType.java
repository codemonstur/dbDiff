package dbdiff.pojos.db;

import java.util.Objects;

/**
 * This class defines the data type of a database column.  It contains integer and String descriptions of a type.
 */
public class ColumnType {
    private final int m_type;
    private final String m_typeCode;

    /**
     * @param type an integer description of the data type in SQL
     * @param typeCode a String description of the data type in Java
     */
    public ColumnType(final int type, final String typeCode) {
        m_type = type;
        m_typeCode = typeCode;
    }

    @Override
    public boolean equals(Object t) {
        if (t == null || getClass() != t.getClass()) {
            return false;
        } else {
            ColumnType other = (ColumnType) t;
            return m_type == other.getType() && Objects.equals(m_typeCode, other.getTypeCode());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_type, m_typeCode);
    }

    /**
     * @return an integer that defines a data type, the sql description
     */
    public int getType() {
        return m_type;
    }

    /**
     * @return a String that describes a data type, the Java description
     */
    public String getTypeCode() {
        return m_typeCode;
    }
}
