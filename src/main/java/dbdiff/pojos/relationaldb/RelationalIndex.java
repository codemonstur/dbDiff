package dbdiff.pojos.relationaldb;

import dbdiff.pojos.db.CatalogSchema;

/**
 * A relational table model which contains appropriate columns and indices
 */
public class RelationalIndex extends BaseColumnContainer {
    /**
     * Create a new index.
     * @param catalogSchema catalog/schema.
     * @param name index name.
     */
    public RelationalIndex(CatalogSchema catalogSchema, String name) {
        super(catalogSchema, name);
    }

    /**
     * Create a new index.
     * @param catalog catalog.
     * @param schema schema.
     * @param name index name.
     */
    public RelationalIndex(String catalog, String schema, String name) {
        super(catalog, schema, name);
    }
}
