package dbdiff.business.catalogschema;

import dbdiff.pojos.db.CatalogSchema;

/**
 * Always returns the same instance of catalog/schema
 */
class SimpleCatalogSchemaResolver implements CatalogSchemaResolver {
    private final CatalogSchema m_catalogSchema;

    /**
     * Create a {@link SimpleCatalogSchemaResolver}
     * @param catalogSchema the catalog/schema to use
     */
    public SimpleCatalogSchemaResolver(CatalogSchema catalogSchema) {
        m_catalogSchema = catalogSchema;
    }

    @Override
    public CatalogSchema resolveCatalogSchema(String jdbcDriver, String jdbcUrl) {
        return m_catalogSchema;
    }
}
