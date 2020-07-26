package dbdiff;

import dbdiff.pojos.db.Database;

import java.sql.DriverManager;
import java.sql.SQLException;

import static dbdiff.Analyzer.analyze;
import static dbdiff.Comparator.compare;

public class ComparatorTest {

    public static void main(String[] args) throws SQLException {

        final String urlOld = "jdbc:mariadb://localhost:3306/old";
        final String urlNew = "jdbc:mariadb://localhost:3306/new";

        final var dbOld = loadRelationalDatabase(urlOld, "root", "");
        final var dbNew = loadRelationalDatabase(urlNew, "root", "");

        for (final var diff : compare(dbOld, dbNew)) {
            System.out.println(diff.location + " - " + diff.errorType + " - " + diff.message);
        }

    }

    private static Database loadRelationalDatabase(final String url, final String username
            , final String password) throws SQLException {
        try (final var connection = DriverManager.getConnection(url, username, password)) {
            return analyze(connection.getMetaData(), connection.getCatalog(), connection.getSchema());
        }
    }

}
