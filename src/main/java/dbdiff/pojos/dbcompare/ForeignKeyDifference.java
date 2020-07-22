package dbdiff.pojos.dbcompare;

import dbdiff.pojos.db.ForeignKey;

/**
 * Extends {@link RdbDifference} to hold extended information specific to foreign key errors.
 */
public class ForeignKeyDifference extends RdbDifference {
    private final ForeignKey similarFk;

    /**
     * Create a new foreign key error.
     * @param errorType error type.
     * @param message message.
     * @param similarFk existing foreign key similar to the foreign key being tested.
     */
    public ForeignKeyDifference(RdbDifferenceType errorType, String message, ForeignKey similarFk) {
        super(errorType, message, RdbFoundOnSide.UNSPECIFIED);
        this.similarFk = similarFk;
    }

    /**
     * @return an existing foreign key similar to the foreign key being tested
     */
    public ForeignKey getSimilarFk() {
        return similarFk;
    }
}
