package dbdiff.pojos.db;

/**
 * An enum representing catalog table types (ie potential results of DatabaseMetaData.getTables()) supported by dbDiff.
 */
public enum TableType {
    /** An INDEX-style table */
    INDEX,

    /** A regular user table */
    TABLE,

    /** A View */
    VIEW

    //"SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", and "SYNONYM" not supported.


}
