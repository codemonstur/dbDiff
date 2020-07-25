package dbdiff.error;

import dbdiff.pojos.db.Index;

import java.sql.SQLException;

public final class InvalidIndexCatalogAndSchema extends SQLException {

    public InvalidIndexCatalogAndSchema(final Index index, final String tableName) {
        super("Index " + index.name + " and table " + tableName + " belong to different catalogs or schemas.");
    }

}
