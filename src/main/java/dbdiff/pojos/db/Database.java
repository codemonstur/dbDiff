package dbdiff.pojos.db;

import dbdiff.error.DuplicateTableName;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Database {
    private final Map<String, Table> tablesByName;

    public Database(final Collection<Table> tables) throws DuplicateTableName {
        this.tablesByName = new LinkedHashMap<>(tables.size());
        for (final var table : tables) {
            if (tablesByName.containsKey(table.name)) {
                throw new DuplicateTableName(table);
            }
            tablesByName.put(table.name, table);
        }
    }

    public Collection<Table> getTables() {
        return Collections.unmodifiableCollection(tablesByName.values());
    }

    public Table getTableByName(String tableName) {
        return tablesByName.get(tableName);
    }
}
