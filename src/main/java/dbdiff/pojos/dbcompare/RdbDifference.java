package dbdiff.pojos.dbcompare;

/**
 * Represents a difference between two relational DB schemas.
 */
public class RdbDifference {
    private final RdbDifferenceType errorType;
    private final String message;
    private final RdbFoundOnSide foundOn;

    /**
     * Create a new instance.
     *
     * @param errorType type of schema difference.
     * @param message descriptive message.
     * @param foundOn indicates whether an extra thing was found on the Ref or Test side.
     */
    public RdbDifference(RdbDifferenceType errorType, String message, RdbFoundOnSide foundOn) {
        this.errorType = errorType;
        this.message = message;
        this.foundOn = foundOn;
    }

    /**
     * @return type of the schema difference.
     */
    public RdbDifferenceType getErrorType() {
        return errorType;
    }

    /**
     * @return descriptive message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return found on side.
     */
    public RdbFoundOnSide getFoundOn() {
        return foundOn;
    }
}
