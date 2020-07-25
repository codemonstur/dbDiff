package dbdiff;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import dbdiff.pojos.compare.Difference;
import dbdiff.pojos.compare.ForeignKeyDifference;
import dbdiff.pojos.db.*;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.Map.Entry;

import static dbdiff.pojos.compare.DifferenceMessages.*;
import static dbdiff.pojos.compare.DifferenceType.*;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public enum Comparator {;

    public static List<Difference> compare(final Database oldDb, final Database newDb) {
        final var differences = new LinkedList<Difference>();

        // First check every test table exists in the reference db
        for (final var newTable : newDb.getTables()) {
            final var oldTable = oldDb.getTableByName(newTable.name);
            if (oldTable == null) {
                differences.add(newUnexpectedTable(newTable));
            } else {
                compareRelationalTables(differences, oldTable, newTable);
            }
        }

        // Check every reference table exists in the test db
        for (final var oldTable : oldDb.getTables()) {
            final var newTable = newDb.getTableByName(oldTable.name);
            if (newTable == null) {
                differences.add(newMissingTable(oldTable));
            }
        }

        return differences;
    }

    public static void compareRelationalTables(final List<Difference> differences, final Table oldTable, final Table newTable) {
        comparePrimaryKeys(differences, oldTable, newTable);
        compareColumns(differences, oldTable, newTable);
        compareForeignKeys(differences, oldTable, newTable);
        compareIndices(differences, oldTable, newTable);
    }

    private static void comparePrimaryKeys(final List<Difference> differences, final Table oldTable, final Table newTable) {
        if (isEmpty(oldTable.getPrimaryKeyColumns())) {
            if (isNotEmpty(newTable.getPrimaryKeyColumns()))
                differences.add(newUnexpectedPrimaryKey(newTable));
        } else if (isEmpty(newTable.getPrimaryKeyColumns())) {
            if (isNotEmpty(oldTable.getPrimaryKeyColumns()))
                differences.add(newMissingPrimaryKey(oldTable));
        } else if (!oldTable.getPrimaryKeyColumns().equals(newTable.getPrimaryKeyColumns())) {
            differences.add(newMisconfiguredPrimaryKey(oldTable, newTable));
        }
    }

    private static void compareColumns(final List<Difference> differences, final Table refT, final Table testT) {
        //First check every test column exists in the reference table
        for (Column testC : testT.getColumns()) {
            Column refC = refT.getColumnByName(testC.name);
            if (refC == null) {
                differences.add(newUnexpectedColumn(testT, testC));
            } else {
                //Column is expected.  Check the column properties
                if (!Objects.equal(refC.columnType.typeId, testC.columnType.typeId)) {
                    // if the codes are different but the type names match, issue a warning
                    if (StringUtils.isNotEmpty(refC.columnType.typeCode) && refC.columnType.typeCode.equals(testC.columnType.typeCode)) {
                        differences.add(newColumnTypeWarning(testT, testC, refC));
                    } else {
                        differences.add(newColumnTypeMismatch(testT, testC, refC));
                    }
                }
                if (!Objects.equal(refC.defaultValue, testC.defaultValue)) {
                    differences.add(newColumnDefaultMismatch(testT, testC, refC));
                }
                if (!Objects.equal(refC.isNullable, testC.isNullable)) {
                    differences.add(newColumnNullableMismatch(testT, testC, refC));
                }
                if (refC.columnSize != null && testC.columnSize != null && !refC.columnSize.equals(testC.columnSize)) {
                    differences.add(newColumnSizeMismatch(testT, testC, refC));
                }
                if (!Objects.equal(refC.ordinal, testC.ordinal)) {
                    //TODO: Turn this into a warning?
                    differences.add(newColumnOrdinalMismatch(testT, testC, refC));
                }
            }
        }

        // Missing Columns: Check every ref col exists in test table
        for (Column refC : refT.getColumns()) {
            if (testT.getColumnByName(refC.name) == null) {
                differences.add(newMissingColumn(testT, refC));
            }
        }
    }

    private static ForeignKeyDifference getUnexpectedForeignKeyDifference(final ForeignKey testFk
            , final Table testT, final Table refT) {
        Set<ForeignKey> refFksByName = refT.getFksByName(testFk.fkName);

        if (!refFksByName.isEmpty()) {
            for (ForeignKey refFk : refFksByName) {
                if (refFk.equalsFrom(testFk) && refFk.equalsReference(testFk)) {
                    if (refFk.keySeq.equals(testFk.keySeq)) {
                        //FK with the same signature, name, and sequence number... something else is wrong
                        return new ForeignKeyDifference(UNKNOWN_FOREIGN_KEY_DIFFERENCE,
                                "Test fk \"" + testFk + "\" has unknown difference with fk \""
                                        + refFk + "\".  Check the fk .equals() method and its hash-generation.", refFk);
                    } else {
                        //FK with the same signature and name, but wrong key sequence
                        return new ForeignKeyDifference(FOREIGN_KEY_SEQUENCE_MISMATCH,
                                "Test fk '" + testFk.fkName + "' in table '" + testT.name + "' has"
                                        + " wrong key sequence. Expected '" + refFk.keySeq + "' but got '"
                                        + testFk.keySeq + "'", refFk);
                    }
                }
            }
            // No reference key by this name has the same to and from.  Misconfigured key.
            String matchingFkNames = Joiner.on(", ").join(refFksByName);

            return new ForeignKeyDifference(MISCONFIGURED_FOREIGN_KEY,
                    "Test fk \"" + testFk + "\" has the same name as the following reference FK "
                            + "constraint(s) but different signature: " + matchingFkNames, null);
        } else {
            //Try to find a match based on reference
            Set<ForeignKey> refFksByRefCol = refT.getFksByReferencedCol(testFk.pkCatalogAndSchema.catalog, testFk.pkCatalogAndSchema.schema,
                    testFk.pkTable, testFk.pkColumn);
            if (!refFksByRefCol.isEmpty()) {
                for (ForeignKey refFk : refFksByRefCol) {
                    if (refFk.equalsFrom(testFk)) {
                        // We have a fk with same signature
                        if (refFk.fkName.equals(testFk.fkName)) {
                            //Same signature and name, unknown difference
                            return new ForeignKeyDifference(UNKNOWN_FOREIGN_KEY_DIFFERENCE,
                                    "Test fk \"" + testFk + "\" has unknown difference with fk \""
                                            + refFk + "\".  Check the fk .equals() method and its hash-generation.", refFk);
                        } else {
                            //Same signature but different name: misnamed FK
                            return new ForeignKeyDifference(MISNAMED_FOREIGN_KEY,
                                    "Test fk \"" + testFk + "\" looks the same as the following fk but wrong"
                                            + " name: \"" + refFk + "\".", refFk);
                        }
                    }
                }

                String matchingFks = Joiner.on(", ").join(refFksByRefCol);

                return new ForeignKeyDifference(MISCONFIGURED_FOREIGN_KEY,
                        "Test fk \"" + testFk + "\" references the same columns as the following reference FK "
                                + "constraint(s) but applies to a different column: " + matchingFks, null);

            } else {
                //Unexpected FK
                return new ForeignKeyDifference(UNEXPECTED_FOREIGN_KEY,
                        "Test foreign key \"" + testFk + "\" is unexpected!", null);
            }
        }
    }

    private static void compareForeignKeys(List<Difference> differences, Table refT, Table testT) {
        final var refFks = new HashSet<>(refT.getForeignKeys());

        for (ForeignKey testFk : testT.getForeignKeys()) {
            if (!refFks.remove(testFk)) {
                ForeignKeyDifference difference = getUnexpectedForeignKeyDifference(testFk, testT, refT);
                if (difference.similarFk != null) {
                    refFks.remove(difference.similarFk);
                }
                differences.add(difference);
            }
        }

        // Missing FK's: Any test fk that had some partial match against a reference fk would have
        // had the reference fk removed. Any remaining reference fk's are missing ones.
        for (ForeignKey fk : refFks) {
            differences.add(newMissingForeignKey(fk));
        }
    }

    private static void compareIndices(final List<Difference> differences, final Table refT, final Table testT) {
        var refIndices = ArrayListMultimap.create(refT.getIndicesByColumns());

        for (final Entry<List<String>, Collection<Index>> entry : testT.getIndicesByColumns().asMap().entrySet()) {
            var matchingRefIndices = refIndices.removeAll(entry.getKey());
            if (isEmpty(matchingRefIndices)) {
                for (Index testIndex : entry.getValue()) {
                    differences.add(newUnexpectedTestIndex(testIndex, testT));
                }
            } else {
                int testIndicesWithUnknownNames = 0;
                int refIndicesWithUnknownNames = 0;
                Set<String> testIndexNames = Sets.newHashSet();
                Set<String> refIndexNames = Sets.newHashSet();

                for (Index refIndex : matchingRefIndices) {
                    if (refIndex.name == null) {
                        refIndicesWithUnknownNames++;
                    } else {
                        refIndexNames.add(refIndex.name);
                    }
                }

                for (Index testIndex : entry.getValue()) {
                    if (testIndex.name == null) {
                        testIndicesWithUnknownNames++;
                    } else {
                        if (!refIndexNames.remove(testIndex.name)) {
                            testIndexNames.add(testIndex.name);
                        }
                    }
                }

                if (refIndicesWithUnknownNames == 0 && !testIndexNames.isEmpty()) {
                    for (String testIndexName : testIndexNames) {
                        differences.add(newUnexpectedTestIndex(testIndexName, entry.getKey(), testT));
                    }
                } else if (testIndexNames.size() > refIndicesWithUnknownNames) {
                    differences.add(newUnexpectedAnonymousIndexes((testIndexNames.size() - refIndicesWithUnknownNames),
                            testIndexNames, entry.getKey(), testT));
                }

                if (testIndicesWithUnknownNames == 0 && !refIndexNames.isEmpty()) {
                    for (String refIndexName : refIndexNames) {
                        differences.add(newMissingIndex(refIndexName, entry.getKey(), refT));
                    }
                } else if (refIndexNames.size() > testIndicesWithUnknownNames) {
                    differences.add(newMissingAnonymousIndexes(refIndexNames.size() - testIndicesWithUnknownNames,
                            refIndexNames, entry.getKey(), refT));
                }
            }
        }
    }

}
