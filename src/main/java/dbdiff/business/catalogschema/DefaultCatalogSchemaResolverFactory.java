package dbdiff.business.catalogschema;

import com.google.common.collect.ImmutableMap;
import dbdiff.pojos.db.CatalogSchema;

/**
 * This abstract factory wires up the default catalog/schema resolver.
 */
public class DefaultCatalogSchemaResolverFactory {
    /**
     * Creates the default catalog/schema resolver
     * @return default catalog/schema resolver
     */
    public static CatalogSchemaResolver getCatalogSchemaResolver() {
        return new DelegatingByDriverCatalogSchemaResolverImpl(ImmutableMap.of("org.postgresql.Driver",
                new SimpleCatalogSchemaResolver(CatalogSchema
                        .defaultCatalogSchema()),
                "net.sourceforge.jtds.jdbc.Driver",
                new SQLServerCatalogSchemaResolver()));
    }
}
