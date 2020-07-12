package dbdiff.pojos.relationalDb;

import dbdiff.pojos.error.InconsistentSchemaException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Serializable collection of RelationalTables, representing a db.
 *
 * Note that this is designed to work wrt to a single schema or catalog!
 */
public class RelationalDatabase {
    private final Map<String, RelationalTable> m_tablesByName;

    /**
     * Construct a new instance.
     * @param tables ordered collection of tables.
     */
    public RelationalDatabase(Collection<RelationalTable> tables) {
        m_tablesByName = new LinkedHashMap<>(tables.size());
        for (RelationalTable rt : tables) {
            if (m_tablesByName.containsKey(rt.getName())) {
                throw new InconsistentSchemaException("A RelationalDatabase supports only unique table names of tables of the same "
                        + "catalog/schema. Non-unique name found: " + rt.getName());
            }
            m_tablesByName.put(rt.getName(), rt);
        }
    }

    /**
     * @return ordered collection of tables in this database.
     */
    public Collection<RelationalTable> getTables() {
        return Collections.unmodifiableCollection(m_tablesByName.values());
    }

    /**
     * Gets a specific table by name.
     * @param tableName the name of the table.
     * @return the table with the given name.
     */
    public RelationalTable getTableByName(String tableName) {
        return m_tablesByName.get(tableName);
    }
}
