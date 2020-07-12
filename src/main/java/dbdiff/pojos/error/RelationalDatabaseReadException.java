package dbdiff.pojos.error;

/**
 * Indicates a communication error with the database while reading the schema (e.g. caused
 * by a SQLException).
 */
public class RelationalDatabaseReadException extends RuntimeException {
    public RelationalDatabaseReadException(String message, Throwable cause) {
        super(message, cause);
    }
    public RelationalDatabaseReadException(Throwable cause) {
        super(cause);
    }
}
