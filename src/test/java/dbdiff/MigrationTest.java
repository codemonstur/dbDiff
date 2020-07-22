package dbdiff;

import dbdiff.pojos.relationaldb.RelationalDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;

import static dbdiff.ConcurrentRDBuilder.createRelationalDatabase;
import static dbdiff.RdbDiffEngine.compareRelationalDatabase;

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
            return createRelationalDatabase(connection.getMetaData(), connection.getCatalog(), connection.getSchema());
        }
    }

}
