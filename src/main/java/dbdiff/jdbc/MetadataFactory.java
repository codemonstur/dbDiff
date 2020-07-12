package dbdiff.jdbc;

import java.io.Closeable;
import java.sql.DatabaseMetaData;

/**
 * API for retrieving database metadata.
 */
public interface MetadataFactory extends Closeable {
    DatabaseMetaData getMetadata();
}
