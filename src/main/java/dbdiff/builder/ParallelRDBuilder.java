package dbdiff.builder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dbdiff.jdbc.MetadataFactory;
import dbdiff.pojos.db.CatalogSchema;
import dbdiff.pojos.db.ColumnType;
import dbdiff.pojos.db.TableType;
import dbdiff.pojos.db.Column;
import dbdiff.pojos.db.ForeignKey;
import dbdiff.pojos.error.InconsistentSchemaException;
import dbdiff.pojos.relationalDb.RelationalDatabase;
import dbdiff.pojos.relationalDb.RelationalIndex;
import dbdiff.pojos.relationalDb.RelationalTable;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

import static dbdiff.util.Concurrency.runInParallel;

public class ParallelRDBuilder implements RelationalDatabaseBuilder {
    private final MetadataFactory metadataFactory;
    private final ExecutorService executor = new ForkJoinPool();

    public ParallelRDBuilder(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    @Override
    public RelationalDatabase createRelationalDatabase(CatalogSchema catalogSchema) throws SQLException {
        List<RelationalTable> tables;
        try {
            tables = getAllTables(catalogSchema);
        } catch (SQLException e) {
            throw new SQLException("could not read table information", e);
        }

        // build columns, foreign and primary keys in parallel
        runInParallel(executor, Collections2.transform(tables, table -> (Callable<Void>) () -> {
            table.setColumns(getColumns(table));
            table.setFks(new HashSet<>(getForeignKeys(table)));
            table.setPkColumns(getPrimaryKeyColumns(table));
            table.setIndices(getIndices(table));
            return null;
        }));

        return new RelationalDatabase(tables);
    }

    private List<RelationalTable> getAllTables(final CatalogSchema catalogSchema) throws SQLException {
        // Get the ResultSet of tables
        String[] tableTypes = {TableType.TABLE.name()};
        ResultSet rs = doGetTablesQuery(catalogSchema, tableTypes);

        // Build a set of Tables
        List<RelationalTable> tables = new ArrayList<RelationalTable>();

        while (rs.next()) {
            RelationalTable table = new RelationalTable(new CatalogSchema(rs.getString(1), rs.getString(2)), rs.getString(3));

            table.setType(rs.getString(4));
            table.setTypeName(rs.getString(5));

            tables.add(table);
        }

        return tables;
    }

    protected ResultSet doGetTablesQuery(CatalogSchema catalogSchema, String[] tableTypes) throws SQLException {
        return metadataFactory.getMetadata().getTables(catalogSchema.getCatalog(), catalogSchema.getSchema(), null, tableTypes);
    }

    private List<Column> getColumns(RelationalTable table) throws SQLException {
        ResultSet columnResultSet = metadataFactory.getMetadata().getColumns(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName(), null);

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

    private List<ForeignKey> getForeignKeys(RelationalTable table) throws SQLException {
        ResultSet fkResultSet = metadataFactory.getMetadata().getImportedKeys(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName());
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

    private List<RelationalIndex> getIndices(RelationalTable table) throws SQLException {
        List<RelationalIndex> indices = new ArrayList<>();

        // maps index name to column names
        Multimap<String, String> idxColumns = ArrayListMultimap.create();

        // one row per index-column pair
        ResultSet rs = metadataFactory.getMetadata().getIndexInfo(table.getCatalogSchema().getCatalog(),
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

    private List<String> getPrimaryKeyColumns(RelationalTable table) throws SQLException {
        Map<Short, String> primaryKeys = new TreeMap<>();
        ResultSet rs = metadataFactory.getMetadata().getPrimaryKeys(table.getCatalogSchema().getCatalog(), table.getCatalogSchema().getSchema(), table.getName());
        while (rs.next()) {
            primaryKeys.put(rs.getShort(5), rs.getString(4));
        }
        return Lists.newArrayList(primaryKeys.values());
    }

}