package dbdiff.pojos.compare;

import dbdiff.pojos.db.ForeignKey;

public final class ForeignKeyDifference extends Difference {
    public final ForeignKey similarFk;

    public ForeignKeyDifference(final DifferenceType type, final String message, final ForeignKey similarFk) {
        super(type, message, FoundOnSide.UNSPECIFIED);
        this.similarFk = similarFk;
    }
}
