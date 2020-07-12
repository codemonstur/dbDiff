package dbdiff.business.dbcompare;

/**
 * Represents a difference between two relational DB schemas.
 */
public class RdbCompareError {
    private final RdbCompareErrorType errorType;
    private final String message;
    private final RdbFoundOnSide foundOn;

    /**
     * Create a new instance.
     *
     * @param errorType type of schema difference.
     * @param message descriptive message.
     * @param foundOn indicates whether an extra thing was found on the Ref or Test side.
     */
    public RdbCompareError(RdbCompareErrorType errorType, String message, RdbFoundOnSide foundOn) {
        this.errorType = errorType;
        this.message = message;
        this.foundOn = foundOn;
    }

    /**
     * @return type of the schema difference.
     */
    public RdbCompareErrorType getErrorType() {
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
