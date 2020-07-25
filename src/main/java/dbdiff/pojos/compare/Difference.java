package dbdiff.pojos.compare;

public class Difference {

    public final DifferenceType errorType;
    public final String message;
    public final FoundOnSide location;

    public Difference(DifferenceType errorType, String message, FoundOnSide location) {
        this.errorType = errorType;
        this.message = message;
        this.location = location;
    }

    public enum FoundOnSide { FOUND_ON_NEW, UNSPECIFIED, FOUND_ON_OLD }


}
