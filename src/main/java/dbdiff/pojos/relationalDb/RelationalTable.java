package dbdiff.pojos.relationalDb;

import com.google.common.collect.*;
import dbdiff.pojos.db.CatalogSchema;
import dbdiff.pojos.db.ForeignKey;
import dbdiff.pojos.error.InconsistentSchemaException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A model of a table possibly containing indexes and foreign keys
 *
 * Note: Currently supports only single-row foreign keys and unique index names (ie from a single schema/catalog)
 */
public class RelationalTable extends BaseColumnContainer {
    private String m_type;
    private String m_typeName;
    private Multimap<List<String>, RelationalIndex> m_indicesByColumns;
    private Set<ForeignKey> m_fks;
    private SetMultimap<String, ForeignKey> m_fksByName; //An internal search index of fk's by name
    //Note that composite keys have the same name
    private SetMultimap<String, ForeignKey> m_fksByTableColumn; //An internal search index of fk's by the "catalog.schema.table.col"
    //being referenced
    private List<String> m_pkColumns;
    /**
     * Create a new table.
     * @param catalogSchema catalog/schema.
     * @param name table name.
     */
    public RelationalTable(CatalogSchema catalogSchema, String name) {
        super(catalogSchema, name);
    }
    /**
     * Create a new table.
     * @param catalog catalog.
     * @param schema schema.
     * @param name table name.
     */
    public RelationalTable(String catalog, String schema, String name) {
        super(catalog, schema, name);
    }

    /**
     * Get the indices. Do NOT modify these indices -- doing so will mess up internal search indexes!
     * @return Returns the indices
     */
    public Collection<RelationalIndex> getIndices() {
        return m_indicesByColumns.values();
    }

    /**
     * Set the indices.
     * @param indices The indices to set
     * @throws InconsistentSchemaException If adding an index not recognized by the current table
     */
    public void setIndices(List<RelationalIndex> indices) throws InconsistentSchemaException {
        ImmutableMultimap.Builder<List<String>, RelationalIndex> indexMapBuilder = ImmutableListMultimap.builder();

        if (getName() == null) {
            throw new InconsistentSchemaException("Trying to add indices without setting a table!");
        } else {
            for (RelationalIndex ri : indices) {
                if (!getCatalogSchema().equals(ri.getCatalogSchema())) {
                    throw new InconsistentSchemaException("Index " + ri.getName() + " and table " + getName() + " belong to different catalogs or schemas.");
                }

                indexMapBuilder.put(ri.getColumnNames(), ri);
            }
        }
        m_indicesByColumns = indexMapBuilder.build();
    }

    /**
     * Get the multimap that maps lists of column names to the indices spanning those columns
     * @return the multimap that maps lists of column names to the indices spanning those columns
     */
    public Multimap<List<String>, RelationalIndex> getIndicesByColumns() {
        return m_indicesByColumns;
    }

    public String getType() {
        return m_type;
    }

    public void setType(String type) {
        m_type = type;
    }

    public String getTypeName() {
        return m_typeName;
    }

    public void setTypeName(String typeName) {
        m_typeName = typeName;
    }

    /**
     * Gets a COPY of the current table's fk.  If you add/remove any, you must set the new collection so that internal search indexes
     * can be reconstructed.
     * @return Returns a copy of the table's fks
     */
    public List<ForeignKey> getFks() {
        return new LinkedList<>(m_fks);
    }

    /**
     * Set the fks.
     * @param fks The fks to set
     * @throws InconsistentSchemaException If mismatch between the added fk's and the table
     */
    public void setFks(Set<ForeignKey> fks) throws InconsistentSchemaException {
        m_fks = fks;
        m_fksByName = HashMultimap.create();
        m_fksByTableColumn = HashMultimap.create();

        // Check validity of fk's and then add them to the search index
        for (ForeignKey fk : fks) {
            if (!getCatalogSchema().equals(fk.getFkCatalogSchema()) || !getName().equals(fk.getFkTable())) {
                throw new InconsistentSchemaException("Foreign key " + fk + " does not match table " + getName());
            }

            //The fk is valid.  Add it to the search indices
            m_fksByName.put(fk.getFkName(), fk);

            String key = fk.getPkCatalogSchema().getCatalog() + "." + fk.getPkCatalogSchema().getSchema() + "." + fk.getPkTable() + "." + fk.getPkColumn();
            m_fksByTableColumn.put(key, fk);
        }
    }

    /**
     * Retrieve a table's foreign keys by a constraint name.
     * @param name A foreign key name
     * @return a set containing matching ForeignKeys
     */
    public Set<ForeignKey> getFksByName(String name) {
        return m_fksByName.get(name);
    }

    /**
     * Get primary key columns
     * @return primary key columns
     */
    public List<String> getPkColumns() {
        return m_pkColumns;
    }

    /**
     * Set primary key columns
     * @param pkColumns the primary key columns to set
     */
    public void setPkColumns(List<String> pkColumns) {
        m_pkColumns = pkColumns;
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
        return m_fksByTableColumn.get(catalog + "." + schema + "." + table + "." + column);
    }
}
