package dbdiff;

import dbdiff.pojos.compare.Difference;
import dbdiff.pojos.db.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static dbdiff.Analyzer.analyze;
import static dbdiff.Comparator.compare;
import static dbdiff.util.SplitSQL.splitSQL;
import static dbdiff.util.Validation.isNullOrEmpty;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class MysqlMigrationTest {

    private static final String
        BASE_URL = "jdbc:mariadb://localhost:3306/",
        USERNAME = "root",
        PASSWORD = "",
        SQL_CREATE_DB = "CREATE DATABASE ",
        SQL_DROP_DB = "DROP DATABASE ";

    public static void main(final String... args) throws IOException, SQLException {
        testMigrationScripts(loadResources("/database1"));
        testMigrationScripts(loadResources("/database2"));
    }

    private static void testMigrationScripts(final List<String> migrationScripts) throws SQLException {
        final String oldDb = UUID.randomUUID().toString().replace("-", "");
        final String newDb = UUID.randomUUID().toString().replace("-", "");
        createTemporaryDatabases(oldDb, newDb);

        final String oldUrl = BASE_URL + oldDb;
        final String newUrl = BASE_URL + newDb;
        for (int i = 0; i < migrationScripts.size(); i++) {
            final var migrationQueries = splitSQL(migrationScripts.get(i), ';');
            executeDbQueries(BASE_URL + oldDb, migrationQueries);

            final List<Difference> differences = listDbDifferences(oldUrl, newUrl);
            final String newScript = MysqlMigration.newMigrationScript(differences);
            if (!checkQueriesEqual(migrationQueries, splitSQL(removeNewLines(newScript), ';'))) {
                System.exit(1);
            }

            executeDbQueries(BASE_URL + newDb, migrationQueries);
        }

        deleteTemporaryDatabases(oldDb, newDb);
    }

    private static boolean checkQueriesEqual(final List<String> first, final List<String> second) {
//        if (first.size() != second.size()) return false;
        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).equals(second.get(i))) {
                System.out.println("Queries are different");
                System.out.println(first.get(i));
                System.out.println(second.get(i));
                System.out.flush();
                return false;
            }
        }
        return false;
    }

    private static String removeNewLines(String input) {
        return input.replace("\n", "").replace("\r", "");
    }

    private static List<Difference> listDbDifferences(final String urlOld, final String urlNew) throws SQLException {
        final var dbOld = loadRelationalDatabase(urlOld, USERNAME, PASSWORD);
        final var dbNew = loadRelationalDatabase(urlNew, USERNAME, PASSWORD);
        return compare(dbOld, dbNew);
    }

    private static Database loadRelationalDatabase(final String url, final String username
        , final String password) throws SQLException {
        try (final var connection = DriverManager.getConnection(url, username, password)) {
            return analyze(connection.getMetaData(), connection.getCatalog(), connection.getSchema());
        }
    }

    private static void createTemporaryDatabases(final String... dbNames) throws SQLException {
        final var queries = Arrays.stream(dbNames).map(dbName -> SQL_CREATE_DB + dbName).collect(toList());
        executeDbQueries(BASE_URL, queries);
    }
    private static void deleteTemporaryDatabases(final String... dbNames) throws SQLException {
        final var queries = Arrays.stream(dbNames).map(dbName -> SQL_DROP_DB + dbName).collect(toList());
        executeDbQueries(BASE_URL, queries);
    }
    private static void executeDbQueries(final String dbUrl, final List<String> queries) throws SQLException {
        try (final var connection = DriverManager.getConnection(dbUrl, USERNAME, PASSWORD)) {
            for (final var query : queries) {
                if (isNullOrEmpty(query)) continue;

                connection.createStatement().executeQuery(query);
            }
        }
    }

    private static List<String> loadResources(final String classpathDir) throws IOException {
        final var migrationScripts = new ArrayList<String>();

        try (final var br = new BufferedReader(new InputStreamReader(MysqlMigrationTest.class.getResourceAsStream(classpathDir)))) {
            for (String resource; (resource = br.readLine()) != null;) {
                migrationScripts.add(loadResource(classpathDir + "/" + resource));
            }
        }

        return migrationScripts;
    }

    private static String loadResource(final String classpathFile) throws IOException {
        try (final var is = MysqlMigrationTest.class.getResourceAsStream(classpathFile)) {
            if (is == null) throw new IOException("Missing resource " + classpathFile);
            try (final var reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(joining(""));
            }
        }
    }

}
