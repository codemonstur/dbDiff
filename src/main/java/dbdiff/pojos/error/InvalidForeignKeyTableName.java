package dbdiff.pojos.error;

import dbdiff.pojos.db.ForeignKey;

import java.sql.SQLException;

public final class InvalidForeignKeyTableName extends SQLException {

    public InvalidForeignKeyTableName(final ForeignKey foreignKey, final String name) {
        super("Foreign key table name for " + foreignKey + " does not match containing table name " + name);
    }

}
