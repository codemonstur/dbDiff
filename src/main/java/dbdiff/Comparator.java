package dbdiff;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import dbdiff.pojos.compare.Difference;
import dbdiff.pojos.compare.ForeignKeyDifference;
import dbdiff.pojos.db.*;

import java.util.*;

import static dbdiff.pojos.compare.DifferenceMessages.*;
import static dbdiff.pojos.compare.DifferenceType.*;
import static dbdiff.util.Validation.isEmpty;
import static dbdiff.util.Validation.isNullOrEmpty;

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
            if (!isEmpty(newTable.getPrimaryKeyColumns()))
                differences.add(newUnexpectedPrimaryKey(newTable));
        } else if (isEmpty(newTable.getPrimaryKeyColumns())) {
            if (!isEmpty(oldTable.getPrimaryKeyColumns()))
                differences.add(newMissingPrimaryKey(oldTable));
        } else if (!oldTable.getPrimaryKeyColumns().equals(newTable.getPrimaryKeyColumns())) {
            differences.add(newMisconfiguredPrimaryKey(oldTable, newTable));
        }
    }

    private static void compareColumns(final List<Difference> differences, final Table oldTable, final Table newTable) {
        // First check every test column exists in the reference table
        for (final Column newColumn : newTable.getColumns()) {
            final Column refC = oldTable.getColumnByName(newColumn.name);
            if (refC == null) {
                differences.add(newUnexpectedColumn(newTable, newColumn));
            } else {
                // Column is expected. Check the column properties
                if (!Objects.equals(refC.columnType.typeId, newColumn.columnType.typeId)) {
                    // if the codes are different but the type names match, issue a warning
                    if (!isNullOrEmpty(refC.columnType.typeCode) && refC.columnType.typeCode.equals(newColumn.columnType.typeCode)) {
                        differences.add(newColumnTypeWarning(newTable, newColumn, refC));
                    } else {
                        differences.add(newColumnTypeMismatch(newTable, newColumn, refC));
                    }
                }
                if (!Objects.equals(refC.defaultValue, newColumn.defaultValue)) {
                    differences.add(newColumnDefaultMismatch(newTable, newColumn, refC));
                }
                if (!Objects.equals(refC.isNullable, newColumn.isNullable)) {
                    differences.add(newColumnNullableMismatch(newTable, newColumn, refC));
                }
                if (refC.columnSize != null && newColumn.columnSize != null && !refC.columnSize.equals(newColumn.columnSize)) {
                    differences.add(newColumnSizeMismatch(newTable, newColumn, refC));
                }
                if (!Objects.equals(refC.ordinal, newColumn.ordinal)) {
                    //TODO: Turn this into a warning?
                    differences.add(newColumnOrdinalMismatch(newTable, newColumn, refC));
                }
            }
        }

        // Missing Columns: Check every ref col exists in test table
        for (final var oldColumn : oldTable.getColumns()) {
            if (newTable.getColumnByName(oldColumn.name) == null) {
                differences.add(newMissingColumn(newTable, oldColumn));
            }
        }
    }

    private static void compareForeignKeys(final List<Difference> differences, final Table oldTable, final Table newTable) {
        final var refFks = new HashSet<>(oldTable.getForeignKeys());

        for (final var newForeignKey : newTable.getForeignKeys()) {
            if (!refFks.remove(newForeignKey)) {
                ForeignKeyDifference difference = getUnexpectedForeignKeyDifference(newForeignKey, newTable, oldTable);
                if (difference.similarFk != null) {
                    refFks.remove(difference.similarFk);
                }
                differences.add(difference);
            }
        }

        // Missing FK's: Any test fk that had some partial match against a reference fk would have
        // had the reference fk removed. Any remaining reference fk's are missing ones.
        for (final var foreignKey : refFks) {
            differences.add(newMissingForeignKey(foreignKey));
        }
    }

    private static void compareIndices(final List<Difference> differences, final Table oldTable, final Table newTable) {
        final var oldIndices = ArrayListMultimap.create(oldTable.getIndicesByColumns());

        for (final var entry : newTable.getIndicesByColumns().asMap().entrySet()) {
            final var matchingOldIndices = oldIndices.removeAll(entry.getKey());
            if (isEmpty(matchingOldIndices)) {
                for (final var newIndex : entry.getValue()) {
                    differences.add(newUnexpectedTestIndex(newIndex, newTable));
                }
            } else {
                int newIndicesWithUnknownNames = 0;
                int oldIndicesWithUnknownNames = 0;
                Set<String> newIndexNames = Sets.newHashSet();
                Set<String> oldIndexNames = Sets.newHashSet();

                for (final var oldIndex : matchingOldIndices) {
                    if (oldIndex.name == null) {
                        oldIndicesWithUnknownNames++;
                    } else {
                        oldIndexNames.add(oldIndex.name);
                    }
                }

                for (final var newIndex : entry.getValue()) {
                    if (newIndex.name == null) {
                        newIndicesWithUnknownNames++;
                    } else {
                        if (!oldIndexNames.remove(newIndex.name)) {
                            newIndexNames.add(newIndex.name);
                        }
                    }
                }

                if (oldIndicesWithUnknownNames == 0 && !newIndexNames.isEmpty()) {
                    for (String testIndexName : newIndexNames) {
                        differences.add(newUnexpectedTestIndex(testIndexName, entry.getKey(), newTable));
                    }
                } else if (newIndexNames.size() > oldIndicesWithUnknownNames) {
                    differences.add(newUnexpectedAnonymousIndexes(
                        newIndexNames.size() - oldIndicesWithUnknownNames,
                            newIndexNames, entry.getKey(), newTable));
                }

                if (newIndicesWithUnknownNames == 0 && !oldIndexNames.isEmpty()) {
                    for (String refIndexName : oldIndexNames) {
                        differences.add(newMissingIndex(refIndexName, entry.getKey(), oldTable));
                    }
                } else if (oldIndexNames.size() > newIndicesWithUnknownNames) {
                    differences.add(newMissingAnonymousIndexes(oldIndexNames.size() - newIndicesWithUnknownNames,
                            oldIndexNames, entry.getKey(), oldTable));
                }
            }
        }
    }

    private static ForeignKeyDifference getUnexpectedForeignKeyDifference(final ForeignKey testFk
            , final Table testT, final Table refT) {
        final var refFksByName = refT.getFksByName(testFk.fkName);

        if (!refFksByName.isEmpty()) {
            for (ForeignKey refFk : refFksByName) {
                if (refFk.equalsFrom(testFk) && refFk.equalsReference(testFk)) {
                    if (refFk.keySeq.equals(testFk.keySeq)) {
                        // FK with the same signature, name, and sequence number... something else is wrong
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
            // No reference key by this name has the same to and from. Misconfigured key.
            String matchingFkNames = Joiner.on(", ").join(refFksByName);

            return new ForeignKeyDifference(MISCONFIGURED_FOREIGN_KEY,
                    "Test fk \"" + testFk + "\" has the same name as the following reference FK "
                            + "constraint(s) but different signature: " + matchingFkNames, null);
        } else {
            // Try to find a match based on reference
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
                            // Same signature but different name: misnamed FK
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

}
