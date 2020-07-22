package dbdiff.pojos.relationaldb;

import dbdiff.pojos.error.InconsistentSchemaException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class RelationalDatabase {
    private final Map<String, RelationalTable> m_tablesByName;

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

    public Collection<RelationalTable> getTables() {
        return Collections.unmodifiableCollection(m_tablesByName.values());
    }

    public RelationalTable getTableByName(String tableName) {
        return m_tablesByName.get(tableName);
    }
}
