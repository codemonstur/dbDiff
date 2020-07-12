package dbdiff.pojos.db;

import java.util.Objects;

/**
 * Encapsulation for the current catalog/schema of interest.
 */
public class CatalogSchema {
    /**
     * Default schema
     */
    public static final String DEFAULT_SCHEMA = "public";

    /**
     * Default catalog
     */
    public static final String DEFAULT_CATALOG = null;
    private final String m_catalog;
    private final String m_schema;
    /**
     * Construct a new {@link CatalogSchema}.
     * @param catalog catalog.
     * @param schema schema.
     */
    public CatalogSchema(String catalog, String schema) {
        m_catalog = catalog;
        m_schema = schema;
    }

    /**
     * @return default catalog/schema.
     */
    public static CatalogSchema defaultCatalogSchema() {
        return new CatalogSchema(DEFAULT_CATALOG, DEFAULT_SCHEMA);
    }

    /**
     * @return the catalog.
     */
    public String getCatalog() {
        return m_catalog;
    }

    /**
     * @return the schema.
     */
    public String getSchema() {
        return m_schema;
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
        return "[" + m_catalog + "." + m_schema + "]";
    }
}
