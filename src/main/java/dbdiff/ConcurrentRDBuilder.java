package dbdiff;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dbdiff.pojos.db.*;
import dbdiff.pojos.error.InconsistentSchemaException;
import dbdiff.pojos.relationaldb.RelationalDatabase;
import dbdiff.pojos.relationaldb.RelationalIndex;
import dbdiff.pojos.relationaldb.RelationalTable;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public enum ConcurrentRDBuilder {;

    public static RelationalDatabase createRelationalDatabase(final DatabaseMetaData metadata, final String catalog
            , final String schema) throws SQLException {
        final var tables = getAllTables(metadata, catalog, schema);

        for (final var table : tables) {
            table.setColumns(getColumns(metadata, table));
            table.setFks(new HashSet<>(getForeignKeys(metadata, table)));
            table.setPkColumns(getPrimaryKeyColumns(metadata, table));
            table.setIndices(getIndices(metadata, table));
        }

        return new RelationalDatabase(tables);
    }

    private static final String[] TABLES_ONLY = new String[] { TableType.TABLE.name() };

    private static List<RelationalTable> getAllTables(final DatabaseMetaData metadata, final String catalog
            , final String schema) throws SQLException {
        final ResultSet rs = metadata.getTables( catalog, schema, null, TABLES_ONLY);

        final List<RelationalTable> tables = new ArrayList<RelationalTable>();

        while (rs.next()) {
            RelationalTable table = new RelationalTable(new CatalogSchema(rs.getString(1), rs.getString(2)), rs.getString(3));

            table.setType(rs.getString(4));
            table.setTypeName(rs.getString(5));

            tables.add(table);
        }

        return tables;
    }

    private static List<Column> getColumns(DatabaseMetaData metadata, RelationalTable table) throws SQLException {
        ResultSet columnResultSet = metadata.getColumns(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName(), null);

        List<Column> columns = new LinkedList<Column>();
        while (columnResultSet.next()) {
            Column column = new Column(columnResultSet.getString(1), columnResultSet.getString(2),
                    columnResultSet.getString(4), columnResultSet.getString(3));

            column.setColumnType(new ColumnType(columnResultSet.getInt(5), columnResultSet.getString(6)));
            column.setColumnSize(columnResultSet.getInt(7));

            //Nullability
            int nullable = columnResultSet.getInt(11);
            column.setIsNullable((DatabaseMetaData.columnNullable == nullable ? true
                    : (DatabaseMetaData.columnNoNulls == nullable ? false : null)));

            column.setDefault(columnResultSet.getString(13));
            column.setOrdinal(columnResultSet.getInt(17));
            columns.add(column);
        }
        return columns;
    }

    private static List<ForeignKey> getForeignKeys(DatabaseMetaData metadata, RelationalTable table) throws SQLException {
        ResultSet fkResultSet = metadata.getImportedKeys(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName());
        List<ForeignKey> fks = new LinkedList<ForeignKey>();
        while (fkResultSet.next()) {
            ForeignKey fk = new ForeignKey();
            fk.setFkName(fkResultSet.getString(12));

            fk.setFkCatalogSchema(new CatalogSchema(fkResultSet.getString(5), fkResultSet.getString(6)));
            fk.setFkTable(fkResultSet.getString(7));
            fk.setFkColumn(fkResultSet.getString(8));

            fk.setPkCatalogSchema(new CatalogSchema(fkResultSet.getString(1), fkResultSet.getString(2)));
            fk.setPkTable(fkResultSet.getString(3));
            fk.setPkColumn(fkResultSet.getString(4));

            fk.setKeySeq(fkResultSet.getString(9));
            fks.add(fk);
        }
        return fks;
    }

    private static List<RelationalIndex> getIndices(DatabaseMetaData metadata, RelationalTable table) throws SQLException {
        List<RelationalIndex> indices = new ArrayList<>();

        // maps index name to column names
        Multimap<String, String> idxColumns = ArrayListMultimap.create();

        // one row per index-column pair
        ResultSet rs = metadata.getIndexInfo(table.getCatalogSchema().getCatalog(),
                table.getCatalogSchema().getSchema(),
                table.getName(), false, false);

        while (rs.next()) {
            String idxName = rs.getString(6);
            Collection<String> columns = idxColumns.get(idxName);
            if (columns.isEmpty()) {
                // build a new index
                RelationalIndex index = new RelationalIndex(table.getCatalogSchema(), rs.getString(6));
                indices.add(index);
            }

            columns.add(rs.getString(9));
        }

        for (RelationalIndex index : indices) {
            List<Column> columns = new ArrayList<>(idxColumns.size());
            for (String idxColumnName : idxColumns.get(index.getName())) {
                // Some db preserved names are double-quoted
                String columnName = idxColumnName.replaceAll("^\"|\"$", "");
                Column column = table.getColumnByName(columnName);

                if (column == null) {
                    throw new InconsistentSchemaException("cannot find column " + columnName + " referenced by index " + index.getName() + " in table " + table.getName());
                }

                columns.add(column);
            }

            index.setColumns(columns);
        }

        return indices;
    }

    private static List<String> getPrimaryKeyColumns(DatabaseMetaData metadata, RelationalTable table) throws SQLException {
        Map<Short, String> primaryKeys = new TreeMap<>();
        ResultSet rs = metadata.getPrimaryKeys(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName());
        while (rs.next()) {
            primaryKeys.put(rs.getShort(5), rs.getString(4));
        }
        return Lists.newArrayList(primaryKeys.values());
    }

}
