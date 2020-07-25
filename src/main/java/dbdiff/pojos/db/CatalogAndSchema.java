package dbdiff.pojos.db;

import java.util.Objects;

public final class CatalogAndSchema {

    public final String catalog;
    public final String schema;

    public CatalogAndSchema(String catalog, String schema) {
        this.catalog = catalog;
        this.schema = schema;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;

        final CatalogAndSchema other = (CatalogAndSchema) obj;
        return Objects.equals(catalog, other.catalog)
            && Objects.equals(schema, other.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), catalog, schema);
    }

    @Override
    public String toString() {
        return "[" + catalog + "." + schema + "]";
    }
}
