package dbdiff;

import dbdiff.pojos.db.Column;
import dbdiff.pojos.db.ForeignKey;
import dbdiff.pojos.db.Database;
import dbdiff.pojos.db.Index;
import dbdiff.pojos.db.Table;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static dbdiff.Analyzer.analyze;

public class AnalyzerTest extends TestCase {

    public void testCreateRelationalDatabase() throws Exception {
        Database database = getDatabase();

        assertEquals("wrong number of tables", 2, database.getTables().size());

        Table personTable = database.getTableByName("PERSON");
        assertNotNull("table PERSON not found", personTable);

        List<Column> cols = new ArrayList<>(personTable.getColumns());
        assertEquals("wrong number of columns", 3, cols.size());
        assertEquals("wrong column", "ID", cols.get(0).name);
        assertEquals("wrong column", Boolean.FALSE, cols.get(0).isNullable);
        assertEquals("wrong column", Types.BIGINT, cols.get(0).columnType.typeId);

        assertEquals("wrong column", "NAME", cols.get(1).name);
        assertEquals("wrong column", Boolean.FALSE, cols.get(1).isNullable);
        assertEquals("wrong column", Types.VARCHAR, cols.get(1).columnType.typeId);

        assertEquals("wrong column", "DOB", cols.get(2).name);
        assertEquals("wrong column", Boolean.FALSE, cols.get(2).isNullable);
        assertEquals("wrong column", Types.TIMESTAMP, cols.get(2).columnType.typeId);

        assertEquals("wrong number PK columns", Arrays.asList("ID"), personTable.getPrimaryKeyColumns());

        assertEquals("wrong number of indices", 2, personTable.getIndices().size());

        Index nameDOB = getIndex(personTable, "NAME", "DOB");
        assertEquals("NAME_DOB_IDX", nameDOB.name);

        getIndex(personTable, "ID");

        Table joinTable = database.getTableByName("PERSON_RELATIVES");

        cols = new ArrayList<>(joinTable.getColumns());
        assertEquals("wrong number of columns", 3, cols.size());

        assertEquals("wrong column", "PERSON_ID", cols.get(0).name);
        assertEquals("wrong column", Boolean.FALSE, cols.get(0).isNullable);
        assertEquals("wrong column", Types.BIGINT, cols.get(0).columnType.typeId);

        assertEquals("wrong column", "RELATIVE_ID", cols.get(1).name);
        assertEquals("wrong column", Boolean.FALSE, cols.get(1).isNullable);
        assertEquals("wrong column", Types.BIGINT, cols.get(1).columnType.typeId);

        assertEquals("wrong column", "RELATIONSHIP", cols.get(2).name);
        assertEquals("wrong column", Boolean.TRUE, cols.get(2).isNullable);
        assertEquals("wrong column", Types.VARCHAR, cols.get(2).columnType.typeId);

        assertEquals("wrong PK columns", Arrays.asList("PERSON_ID", "RELATIVE_ID"), joinTable.getPrimaryKeyColumns());

        getIndex(joinTable, "PERSON_ID", "RELATIVE_ID");

        ForeignKey fkPerson = getForeignKey(joinTable, "FK_PERSON");
        assertEquals("PERSON_ID", fkPerson.fkColumn);
        assertEquals("PERSON_RELATIVES", fkPerson.fkTable);

        assertEquals("ID", fkPerson.pkColumn);
        assertEquals("PERSON", fkPerson.pkTable);

        ForeignKey fkRelative = getForeignKey(joinTable, "FK_RELATIVE");
        assertEquals("RELATIVE_ID", fkRelative.fkColumn);
        assertEquals("PERSON_RELATIVES", fkRelative.fkTable);

        assertEquals("ID", fkRelative.pkColumn);
        assertEquals("PERSON", fkRelative.pkTable);
    }

    private static Database getDatabase() throws Exception {
        try (final var connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")) {
            connection.setAutoCommit(true);

            for (String sqlStatement : readResource("/test-db.sql").split(";")) {
                connection.createStatement().execute(sqlStatement);
            }
            return analyze(connection.getMetaData(), null, "PUBLIC");
        }
    }

    private static String readResource(final String resource) throws IOException {
        try (final var stream = AnalyzerTest.class.getResourceAsStream(resource)) {
            return IOUtils.toString(stream);
        }
    }

    /**
     * Find a unique index that spans a list of columns. Fail if there's no such unique index.
     */
    private static Index getIndex(Table table, String... columnNames) {
        Collection<Index> indices = table.getIndicesByColumns().get(Arrays.asList(columnNames));

        assertEquals("cannot find a unique index for columns " + Arrays.asList(columnNames), 1, indices.size());
        return indices.iterator().next();
    }

    /**
     * Find a unique foreign key by name. Fail if there's no such unique foreign key.
     */
    private static ForeignKey getForeignKey(Table table, String name) {
        Collection<ForeignKey> foreignKeys = table.getFksByName(name);
        assertEquals("cannot find a unique foreign key " + name, 1, foreignKeys.size());
        return foreignKeys.iterator().next();
    }

}
