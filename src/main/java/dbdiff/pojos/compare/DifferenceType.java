package dbdiff.pojos.compare;

public enum DifferenceType {
    /** A testdb is missing a ref table */
    MISSING_TABLE,

    /** A testdb has an unexpected table */
    UNEXPECTED_TABLE,

    /** A testdb table is missing a ref column */
    MISSING_COLUMN,

    /** A test column is of the wrong type */
    COLUMN_TYPE_MISMATCH,

    /** Test column sql type code is wrong but the sql type name is correct **/
    COLUMN_TYPE_WARNING,

    /** A test column has the wrong default */
    COLUMN_DEFAULT_MISMATCH,

    /** A test column has the wrong nullability */
    COLUMN_NULLABLE_MISMATCH,

    /** A test column has the wrong size */
    COLUMN_SIZE_MISMATCH,

    /** A test column has the wrong ordinal */
    COLUMN_ORDINAL_MISMATCH, //TODO: Make this a warning

    /** A test table has an extra column */
    UNEXPECTED_COLUMN,

    /** A test table is missing a FK */
    MISSING_FOREIGN_KEY,

    /** A test table's fk uses a column to reference another table just like a reference fk, but it's named differently from
     * the reference constraint */
    MISNAMED_FOREIGN_KEY,

    /** A test table contains a fk with the same name as a test fk, but it points to a different column */
    MISCONFIGURED_FOREIGN_KEY,

    /** A test FK looks the same as a reference fk, but it has the wrong sequence (implications for composite key's index structure)*/
    FOREIGN_KEY_SEQUENCE_MISMATCH,

    /** A test table has a fk constraint not in the reference table */
    UNEXPECTED_FOREIGN_KEY,

    /** Unexpected fk difference */
    UNKNOWN_FOREIGN_KEY_DIFFERENCE,

    // Index Errors:
    //--------------
    /** A testdb table is missing a ref index */
    MISSING_INDEX,

    /** A testdb table's index is missing a column */
    INDEX_MISSING_COLUMN,

    /** A testdb table has an unexpected index */
    UNEXPECTED_INDEX,

    /** A testdb table's index has an unexpected column */
    UNEXPECTED_INDEX_COLUMN,

    /** An index is in both dbs with same name and column set but different column order */
    WRONG_INDEX_COL_ORDER,

    /** Test table is missing a primary key */
    MISSING_PRIMARY_KEY,
    /** Test table has a primary key but the reference table doesn't */
    UNEXPECTED_PRIMARY_KEY,
    /** Both table have primary keys but they span different columns */
    MISCONFIGURED_PRIMARY_KEY;


}
