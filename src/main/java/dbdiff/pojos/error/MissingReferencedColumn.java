package dbdiff.pojos.error;

import java.sql.SQLException;

public final class MissingReferencedColumn extends SQLException {

    public MissingReferencedColumn(final String columnName, final String indexName, final String tableName) {
        super("cannot find column " + columnName + " referenced by index " + indexName + " in table " + tableName);
    }

}
