package dbdiff.pojos.compare;

import com.google.common.base.Joiner;
import dbdiff.pojos.db.Column;
import dbdiff.pojos.db.ForeignKey;
import dbdiff.pojos.db.Index;
import dbdiff.pojos.db.Table;

import java.util.Collection;
import java.util.Set;

import static dbdiff.pojos.compare.Difference.FoundOnSide.*;
import static dbdiff.pojos.compare.DifferenceType.*;
import static java.util.stream.Collectors.toList;

public enum DifferenceMessages {;

    public static Difference newMissingTable(final Table refT) {
        return new Difference(MISSING_TABLE, "Reference Table '" + refT.name + "' is missing", FOUND_ON_OLD);
    }
    public static Difference newUnexpectedTable(final Table testT) {
        return new Difference(UNEXPECTED_TABLE, "Test table '" + testT.name + "' is not in expected db", FOUND_ON_NEW);
    }
    public static Difference newUnexpectedColumn(final Table testT, final Column testC) {
        return new Difference(UNEXPECTED_COLUMN, "Column '" + testT.name + "." + testC.name + "' is unexpected", FOUND_ON_NEW);
    }
    public static Difference newMissingPrimaryKey(final Table refT) {
        return new Difference(MISSING_PRIMARY_KEY, "Reference primary key " + refT.name + refT.getPrimaryKeyColumns() + " is missing!", FOUND_ON_OLD);
    }
    public static Difference newUnexpectedPrimaryKey(final Table testT) {
        return new Difference(UNEXPECTED_PRIMARY_KEY
            , "Test primary key " + testT.name + testT.getPrimaryKeyColumns() + " is unexpected!", FOUND_ON_NEW);
    }
    public static Difference newMisconfiguredPrimaryKey(final Table refT, final Table testT) {
        return new Difference(MISCONFIGURED_PRIMARY_KEY, "Test primary key " + testT.name + testT.getPrimaryKeyColumns()
                + " differs from reference primary key " + refT.name + refT.getPrimaryKeyColumns(), UNSPECIFIED);
    }
    public static Difference newColumnTypeWarning(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_TYPE_WARNING, "Test column '" + testT.name + "." + testC.name + "' has wrong type. "
                + "Expected '" + refC.columnType.typeId + "/" + refC.columnType.typeCode
                + "' but got '" + testC.columnType.typeId + "/" + testC.columnType.typeCode + "'",
                UNSPECIFIED);
    }
    public static Difference newColumnTypeMismatch(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_TYPE_MISMATCH, "Test column '" + testT.name + "." + testC.name + "' has wrong type. "
                + "Expected '" + refC.columnType.typeId + "/" + refC.columnType.typeCode
                + "' but got '" + testC.columnType.typeId + "/" + testC.columnType.typeCode + "'", UNSPECIFIED);
    }
    public static Difference newColumnDefaultMismatch(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_DEFAULT_MISMATCH, "Test column '" + testT.name + "." + testC.name + "' has wrong Default. "
                + "Expected '" + refC.defaultValue + "' but got '" + testC.defaultValue + "'", UNSPECIFIED);
    }
    public static Difference newColumnNullableMismatch(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_NULLABLE_MISMATCH, "Test column '" + testT.name + "." + testC.name + "' has wrong "
            + "nullability.  Expected '" + refC.isNullable + "' but got '" + testC.isNullable + "'", UNSPECIFIED);
    }
    public static Difference newColumnSizeMismatch(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_SIZE_MISMATCH, "Test column '" + testT.name + "." + testC.name + "' has wrong size. "
            + "Expected '" + refC.columnSize + "' but got '" + testC.columnSize + "'", UNSPECIFIED);
    }
    public static Difference newColumnOrdinalMismatch(final Table testT, final Column testC, final Column refC) {
        return new Difference(COLUMN_ORDINAL_MISMATCH, "Test column '" + testT.name + "." + testC.name + "' has wrong ordinal.  "
            + "Expected '" + refC.ordinal + "' but got '" + testC.ordinal + "'", UNSPECIFIED);
    }
    public static Difference newMissingColumn(final Table testT, final Column refC) {
        return new Difference(MISSING_COLUMN, "Table '" + testT.name + "' is missing column '" + refC.name + "'", FOUND_ON_OLD);
    }
    public static Difference newMissingIndex(final String indexName, final Collection<String> columnNames, final Table refT) {
        return new Difference(MISSING_INDEX, "Reference index \"" + getIndexDescription(indexName, columnNames, refT) + "\" is missing!", FOUND_ON_OLD);
    }
    public static Difference newUnexpectedTestIndex(final Index testIndex, final Table testT) {
        return new Difference(UNEXPECTED_INDEX, "Test index \"" +
            getIndexDescription(testIndex, testT) + "\" is unexpected!", FOUND_ON_NEW);
    }
    public static Difference newUnexpectedTestIndex(final String testIndexName, final Collection<String> columnNames
            , final Table testT) {
        return new Difference(UNEXPECTED_INDEX, "Test index \"" +
            getIndexDescription(testIndexName, columnNames, testT) + "\" is unexpected!", FOUND_ON_NEW);
    }
    public static Difference newMissingForeignKey(final ForeignKey fk) {
        return new Difference(MISSING_FOREIGN_KEY, "Reference foreign key \"" + fk + "\" is missing!", FOUND_ON_OLD);
    }

    public static Difference newMissingAnonymousIndexes(final int number, final Set<String> indexNames
            , final Collection<String> columnNames, final Table refT) {
        final var indexDescriptions = indexNames
            .stream()
            .map(from -> "\"" + getIndexDescription(from, columnNames, refT) + "\"")
            .collect(toList());
        return new Difference(MISSING_INDEX, "At least " + number + " of reference indices "
            + Joiner.on(", ").join(indexDescriptions) + " are missing!", FOUND_ON_OLD);
    }

    public static Difference newUnexpectedAnonymousIndexes(final int number, final Set<String> indexNames
            , final Collection<String> columnNames, final Table testT) {
        final var indexDescriptions = indexNames
            .stream()
            .map(from -> "\"" + getIndexDescription(from, columnNames, testT) + "\"")
            .collect(toList());
        return new Difference(UNEXPECTED_INDEX, "At least " + number + " of test indices "
            + Joiner.on(", ").join(indexDescriptions) + " are unexpected!", FOUND_ON_NEW);
    }


    private static String getIndexDescription(final Index idx, final Table owner) {
        final var indexColumnNames = idx.getColumns().stream().map(column -> column.name).collect(toList());
        return getIndexDescription(idx.name, indexColumnNames, owner);
    }
    private static String getIndexDescription(final String indexName, final Collection<String> columnNames
            , final Table owner) {
        return (indexName == null ? "<UNKNOWN>" : indexName) + "=" + owner.name + "(" + Joiner.on(',').join(columnNames) + ")";
    }

}
