package dbdiff.error;

import dbdiff.pojos.db.Table;

import java.sql.SQLException;

public final class DuplicateTableName extends SQLException {
    public DuplicateTableName(final Table rt) {
        super("A RelationalDatabase supports only unique table names of tables of the same "
                + "catalog/schema. Non-unique name found: " + rt.name);
    }
}
