package dbdiff.pojos.db;

import com.google.common.collect.*;
import dbdiff.pojos.error.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

// TODO Currently supports only single-row foreign keys and unique index names (ie from a single schema/catalog)
public final class Table extends BaseColumnContainer {
    private String type;
    private String typeName;
    private Multimap<List<String>, Index> indicesByColumns;
    private Set<ForeignKey> foreignKeys;
    private List<String> primaryKeyColumns;

    // An internal search index of fk's by name
    private SetMultimap<String, ForeignKey> fksByName;
    // Note that composite keys have the same name
    // An internal search index of fk's by the "catalog.schema.table.col"
    private SetMultimap<String, ForeignKey> foreignKeysByTableColumn;

    public Table(final ResultSet set) throws SQLException {
        super(set.getString(1), set.getString(2), set.getString(3));
        this.type = set.getString(4);
        this.typeName = set.getString(5);
    }

    /**
     * Do NOT modify these indices -- doing so will mess up internal search indexes!
     */
    public Collection<Index> getIndices() {
        return indicesByColumns.values();
    }

    public void setIndices(List<Index> indices) throws MissingTableName, InvalidIndexCatalogAndSchema {
        if (name == null) throw new MissingTableName();

        final var indexMapBuilder = ImmutableListMultimap.<List<String>, Index>builder();
        for (final var index : indices) {
            if (!isCatalogAndSchemaEqual(index)) {
                throw new InvalidIndexCatalogAndSchema(index, name);
            }

            indexMapBuilder.put(index.getColumnNames(), index);
        }
        indicesByColumns = indexMapBuilder.build();
    }

    /**
     * Get the multimap that maps lists of column names to the indices spanning those columns
     * @return the multimap that maps lists of column names to the indices spanning those columns
     */
    public Multimap<List<String>, Index> getIndicesByColumns() {
        return indicesByColumns;
    }


    /**
     * Gets a COPY of the current table's fk.  If you add/remove any, you must set the new collection so that internal search indexes
     * can be reconstructed.
     * @return Returns a copy of the table's fks
     */
    public List<ForeignKey> getForeignKeys() {
        return new LinkedList<>(foreignKeys);
    }

    public void setForeignKeys(final List<ForeignKey> foreignKeys) throws InvalidForeignKeyTableName, InvalidForeignKeyCatalogAndSchema {
        this.foreignKeys = new HashSet<>(foreignKeys);
        fksByName = HashMultimap.create();
        foreignKeysByTableColumn = HashMultimap.create();

        // Check validity of fk's and then add them to the search index
        for (final var foreignKey : foreignKeys) {
            if (!isCatalogAndSchemaEqual(foreignKey)) throw new InvalidForeignKeyCatalogAndSchema(foreignKey, name);
            if (!name.equals(foreignKey.fkTable)) throw new InvalidForeignKeyTableName(foreignKey, name);

            // The fk is valid. Add it to the search indices
            fksByName.put(foreignKey.fkName, foreignKey);

            String key = foreignKey.pkCatalogAndSchema.catalog + "." + foreignKey.pkCatalogAndSchema.schema + "." + foreignKey.pkTable + "." + foreignKey.pkColumn;
            foreignKeysByTableColumn.put(key, foreignKey);
        }
    }

    /**
     * Retrieve a table's foreign keys by a constraint name.
     * @param name A foreign key name
     * @return a set containing matching ForeignKeys
     */
    public Set<ForeignKey> getFksByName(String name) {
        return fksByName.get(name);
    }

    /**
     * Get primary key columns
     * @return primary key columns
     */
    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    /**
     * Set primary key columns
     * @param primaryKeyColumns the primary key columns to set
     */
    public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
        this.primaryKeyColumns = primaryKeyColumns;
    }

    /**
     * Retrieve a table's foreign keys by specifying the column the constraint refers to
     * @param catalog The catalog of the referenced table.  Must be exact match (ie no wildcards)
     * @param schema The schema of the referenced table.  Must be exact match (ie no wildcards)
     * @param table The name of the referenced table.  Must be exact match (ie no wildcards)
     * @param column The column being referenced.  Must be exact match (ie no wildcards)
     * @return All foreign keys matching the specified reference.
     */
    public Set<ForeignKey> getFksByReferencedCol(String catalog, String schema, String table, String column) {
        return foreignKeysByTableColumn.get(catalog + "." + schema + "." + table + "." + column);
    }

}
