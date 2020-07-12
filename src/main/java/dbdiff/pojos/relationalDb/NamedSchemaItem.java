package dbdiff.pojos.relationalDb;

import dbdiff.pojos.db.CatalogSchema;

/**
 * DB schema item with name, catalog, and schema.
 */
public abstract class NamedSchemaItem {
    private final CatalogSchema m_catalogSchema;
    private final String m_name;

    /**
     * Create a new instance.
     * @param catalogSchema catalog/schema.
     * @param name name.
     */
    public NamedSchemaItem(CatalogSchema catalogSchema, String name) {
        m_catalogSchema = catalogSchema;
        m_name = name;
    }

    /**
     * Create a new instance.
     * @param catalog catalog.
     * @param schema schema.
     * @param name name.
     */
    public NamedSchemaItem(String catalog, String schema, String name) {
        this(new CatalogSchema(catalog, schema), name);
    }

    /**
     * @return catalog/schema.
     */
    public CatalogSchema getCatalogSchema() {
        return m_catalogSchema;
    }

    /**
     * @return name.
     */
    public String getName() {
        return m_name;
    }
}
