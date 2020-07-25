package dbdiff.pojos.db;

import java.util.Objects;

public abstract class NamedSchemaItem {
    public final String catalog;
    public final String schema;
    public final String name;

    public NamedSchemaItem(final String catalog, final String schema, final String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public boolean isCatalogAndSchemaEqual(final NamedSchemaItem item) {
        return Objects.equals(catalog, item.catalog)
            && Objects.equals(schema, item.schema);
    }
    public boolean isCatalogAndSchemaEqual(final ForeignKey item) {
        return Objects.equals(catalog, item.fkCatalogAndSchema.catalog)
            && Objects.equals(schema, item.fkCatalogAndSchema.schema);
    }

}
