package dbdiff.error;

import dbdiff.pojos.db.ForeignKey;

import java.sql.SQLException;

public final class InvalidForeignKeyCatalogAndSchema extends SQLException {

    public InvalidForeignKeyCatalogAndSchema(final ForeignKey foreignKey, String name) {
        super("Catalog or schema for " + foreignKey + " does not match containing table catalog and schema");
    }

}
