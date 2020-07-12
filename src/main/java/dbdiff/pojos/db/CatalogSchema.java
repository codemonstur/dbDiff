package dbdiff.pojos.db;

import java.util.Objects;

/**
 * Encapsulation for the current catalog/schema of interest.
 */
public class CatalogSchema {

    public static final String DEFAULT_SCHEMA = "public";
    public static final String DEFAULT_CATALOG = null;

    public static CatalogSchema defaultCatalogSchema() {
        return new CatalogSchema(DEFAULT_CATALOG, DEFAULT_SCHEMA);
    }


    private final String catalog;
    private final String schema;

    public CatalogSchema(String catalog, String schema) {
        this.catalog = catalog;
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }
    public String getSchema() {
        return schema;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            CatalogSchema other = (CatalogSchema) obj;
            return Objects.equals(getCatalog(), other.getCatalog()) && Objects.equals(getSchema(), other.getSchema());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getCatalog(), getSchema());
    }

    @Override
    public String toString() {
        return "[" + catalog + "." + schema + "]";
    }
}
