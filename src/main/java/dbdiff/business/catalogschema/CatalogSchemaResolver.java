package dbdiff.business.catalogschema;

import dbdiff.pojos.db.CatalogSchema;

/**
 * Determines catalog/schema from the jdbc driver and the jdbc url.
 */
public interface CatalogSchemaResolver {
    /**
     * Resolve the catalog/schema
     *
     * @param jdbcDriver driver
     * @param jdbcUrl    url
     * @return catalog/schema
     */
    CatalogSchema resolveCatalogSchema(String jdbcDriver, String jdbcUrl);
}
