package dbdiff.builder;

import dbdiff.pojos.db.CatalogSchema;
import dbdiff.pojos.relationalDb.RelationalDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;

import static dbdiff.builder.ConcurrentRDBuilder.createRelationalDatabase;
import static dbdiff.business.dbcompare.RdbDiffEngine.compareRelationalDatabase;

public class MigrationTest {

    public static void main(String[] args) throws SQLException {

        final String urlOld = "jdbc:mariadb://localhost:3306/old";
        final String urlNew = "jdbc:mariadb://localhost:3306/new";

        final var dbOld = loadRelationalDatabase(urlOld, "root", "");
        final var dbNew = loadRelationalDatabase(urlNew, "root", "");

        for (final var diff : compareRelationalDatabase(dbOld, dbNew)) {
            System.out.println(diff.getFoundOn() + " - " + diff.getErrorType() + " - " + diff.getMessage());
        }

    }

    private static RelationalDatabase loadRelationalDatabase(final String url, final String username
            , final String password) throws SQLException {
        try (final var connection = DriverManager.getConnection(url, username, password)) {
            return createRelationalDatabase(connection.getMetaData(), new CatalogSchema(connection.getCatalog(), connection.getSchema()));
        }
    }
}
