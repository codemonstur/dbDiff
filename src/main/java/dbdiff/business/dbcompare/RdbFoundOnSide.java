package dbdiff.business.dbcompare;

/**
 * Enumeration for possible database comparison errors
 */
public enum RdbFoundOnSide {
    /** Extra thing found on test db */
    TEST,
    /** Unspecified */
    UNSPECIFIED,
    /** Extra thing found on ref db */
    REF
}
