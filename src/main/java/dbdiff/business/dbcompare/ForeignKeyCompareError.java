package dbdiff.business.dbcompare;

import dbdiff.pojos.db.ForeignKey;

/**
 * Extends {@link RdbCompareError} to hold extended information specific to foreign key errors.
 */
public class ForeignKeyCompareError extends RdbCompareError {
    private final ForeignKey similarFk;

    /**
     * Create a new foreign key error.
     * @param errorType error type.
     * @param message message.
     * @param similarFk existing foreign key similar to the foreign key being tested.
     */
    public ForeignKeyCompareError(RdbCompareErrorType errorType, String message, ForeignKey similarFk) {
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
