package dbdiff.builder;

/**
 * Indicates a communication error with the database while reading the schema (e.g. caused
 * by a SQLException).
 */
public class RelationalDatabaseReadException extends RuntimeException {
    /**
     * Create a new exception.
     *
     * @param message message.
     * @param cause   cause.
     */
    public RelationalDatabaseReadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new exception.
     *
     * @param message message.
     */
    public RelationalDatabaseReadException(String message) {
        super(message);
    }

    /**
     * Create a new exception.
     *
     * @param cause cause.
     */
    public RelationalDatabaseReadException(Throwable cause) {
        super(cause);
    }
}
