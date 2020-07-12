package dbdiff.business.catalogschema;

import dbdiff.pojos.db.CatalogSchema;

import java.util.Map;

/**
 * Delegates to another {@link CatalogSchemaResolver} based on the jdbc driver (i.e. the db type).
 */
class DelegatingByDriverCatalogSchemaResolverImpl implements CatalogSchemaResolver {
    private final Map<String, CatalogSchemaResolver> m_resolverMap;

    /**
     * Create a new {@link DelegatingByDriverCatalogSchemaResolverImpl}
     * @param resolverMap resolver map
     */
    public DelegatingByDriverCatalogSchemaResolverImpl(Map<String, CatalogSchemaResolver> resolverMap) {
        m_resolverMap = resolverMap;
    }

    @Override
    public CatalogSchema resolveCatalogSchema(String jdbcDriver, String jdbcUrl) {
        if (jdbcDriver == null) {
            throw new IllegalArgumentException("need to know the jdbc driver to determine catalog/schema");
        }
        CatalogSchemaResolver resolver = m_resolverMap.get(jdbcDriver);
        if (resolver == null) {
            throw new IllegalArgumentException("driver " + jdbcDriver + " is not supported");
        } else {
            return resolver.resolveCatalogSchema(jdbcDriver, jdbcUrl);
        }
    }
}
