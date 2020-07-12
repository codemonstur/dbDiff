package dbdiff.builder;

import dbdiff.pojos.db.CatalogSchema;
import dbdiff.pojos.error.InconsistentSchemaException;
import dbdiff.pojos.relationalDb.RelationalDatabase;

/**
 * Builds a {@link RelationalDatabase} model from a live database.
 */
public interface RelationalDatabaseBuilder {
    /**
     * @param catalogSchema The schema to create a RelationalDatabase for.  Note: must have either catalog or schema defined
     * @return A populated RelationalDatabase object
     * @throws RelationalDatabaseReadException if database communication failed.
     * @throws InconsistentSchemaException     if schema information was inconsistent (see {@link InconsistentSchemaException}).
     */
    RelationalDatabase createRelationalDatabase(CatalogSchema catalogSchema) throws RelationalDatabaseReadException, InconsistentSchemaException;
}
