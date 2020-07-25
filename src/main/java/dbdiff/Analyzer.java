package dbdiff;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dbdiff.pojos.db.*;
import dbdiff.error.MissingReferencedColumn;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public enum Analyzer {;

    public static Database analyze(final DatabaseMetaData metadata, final String catalog
            , final String schema) throws SQLException {
        final var tables = getAllTables(metadata, catalog, schema);

        for (final var table : tables) {
            table.setColumns(getColumns(metadata, table));
            table.setForeignKeys(getForeignKeys(metadata, table));
            table.setPrimaryKeyColumns(getPrimaryKeyColumns(metadata, table));
            table.setIndices(getIndices(metadata, table));
        }

        return new Database(tables);
    }

    // "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM", "INDEX", and "VIEW" not supported.
    private static final String[] REGULAR_TABLES_ONLY = new String[] { "TABLE" };

    private static List<Table> getAllTables(final DatabaseMetaData metadata, final String catalog
            , final String schema) throws SQLException {
        final ResultSet rs = metadata.getTables( catalog, schema, null, REGULAR_TABLES_ONLY);
        return resultSetToList(rs, Table::new);
    }

    private static List<Column> getColumns(DatabaseMetaData metadata, Table table) throws SQLException {
        final var columnResultSet = metadata.getColumns(table.catalog, table.schema, table.name, null);
        return resultSetToList(columnResultSet, Column::new);
    }

    private static List<ForeignKey> getForeignKeys(final DatabaseMetaData metadata, final Table table) throws SQLException {
        final ResultSet fkResultSet = metadata.getImportedKeys(table.catalog, table.schema, table.name);
        return resultSetToList(fkResultSet, ForeignKey::new);
    }

    private static List<Index> getIndices(DatabaseMetaData metadata, Table table) throws SQLException {
        final var rs = metadata.getIndexInfo(table.catalog, table.schema, table.name, false, false);
        final var indexNames = new HashSet<String>();
        final var indexColumns = ArrayListMultimap.<String, String>create();
        loadIndexNamesAndColumnsInto(rs, indexNames, indexColumns);

        final List<Index> indices = new ArrayList<>();
        for (final String name : indexNames) {
            indices.add(new Index(table.catalog, table.schema, name, getIndexColumns(indexColumns.get(name), name, table)));
        }

        return indices;
    }

    private static void loadIndexNamesAndColumnsInto(final ResultSet rs, final Set<String> indexNames
            , final Multimap<String, String> indexColumns) throws SQLException {
        while (rs.next()) {
            final String idxName = rs.getString(6);

            indexNames.add(idxName);
            indexColumns.put(idxName, rs.getString(9));
        }
    }

    private static List<Column> getIndexColumns(final Collection<String> columnNames, final String indexName
            , final Table table) throws MissingReferencedColumn {
        final var columns = new ArrayList<Column>(columnNames.size());

        for (var columnName : columnNames) {
            // Some db preserved names are double-quoted
            columnName = columnName.replaceAll("^\"|\"$", "");

            final var column = table.getColumnByName(columnName);
            if (column == null) throw new MissingReferencedColumn(columnName, indexName, table.name);

            columns.add(column);
        }

        return columns;
    }

    private static List<String> getPrimaryKeyColumns(DatabaseMetaData metadata, Table table) throws SQLException {
        final var primaryKeys = new TreeMap<Short, String>();
        final var rs = metadata.getPrimaryKeys(table.catalog, table.schema, table.name);
        while (rs.next()) {
            primaryKeys.put(rs.getShort(5), rs.getString(4));
        }
        return Lists.newArrayList(primaryKeys.values());
    }


    public interface RecordToObject<T> {
        T convert(ResultSet set) throws SQLException;
    }

    private static <T> List<T> resultSetToList(final ResultSet set, final RecordToObject<T> converter) throws SQLException {
        final var list = new LinkedList<T>();
        while (set.next()) {
            list.add(converter.convert(set));
        }
        return list;
    }

}
