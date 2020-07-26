package dbdiff.pojos.compare;

public class Difference {

    public final DifferenceType errorType;
    public final String message;
    public final FoundOnSide location;
    public final String migrationQuery;

    public Difference(final DifferenceType errorType, final String message, final FoundOnSide location) {
        this(errorType, message, location, "");
    }
    public Difference(final DifferenceType errorType, final String message, final FoundOnSide location
            , final String migrationQuery) {
        this.errorType = errorType;
        this.message = message;
        this.location = location;
        this.migrationQuery = migrationQuery;
    }

    public enum FoundOnSide { FOUND_ON_NEW, UNSPECIFIED, FOUND_ON_OLD }


}
