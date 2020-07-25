package dbdiff.pojos.db;

import java.util.List;

public final class Index extends BaseColumnContainer {

    public Index(final String catalog, final String schema, final String name, final List<Column> columns) {
        super(catalog, schema, name, columns);
    }

}
