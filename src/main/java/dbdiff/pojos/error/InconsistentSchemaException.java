package dbdiff.pojos.error;

/**
 * Indicates that the schema reported by the database is inconsistent (e.g. an index refers
 * to a column that doesn't exist in the table).
 */
public class InconsistentSchemaException extends RuntimeException {
    public InconsistentSchemaException(String message) {
        super(message);
    }
}
