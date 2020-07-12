package dbdiff.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

/**
 * An implementation of {@link MetadataFactory} that creates a connection per thread.
 */
public class ThreadLocalMetadataFactory implements MetadataFactory {
    private final Collection<Connection> m_connections = new Vector<>();
    private final String m_url;
    private final String m_username;
    private final String m_password;
    private final ThreadLocal<DatabaseMetaData> m_threadLocalMetadata = new ThreadLocal<DatabaseMetaData>() {
        @Override
        protected DatabaseMetaData initialValue() {
            try {
                Connection connection = DriverManager.getConnection(m_url, m_username, m_password);
                m_connections.add(connection);
                return connection.getMetaData();
            } catch (SQLException e) {
                throw new RuntimeException("could not retrieve jdbc metadata", e);
            }
        }

    };

    /**
     * Create a new factory.
     * @param url jdbc url.
     * @param username jdbc url.
     * @param password jdbc password.
     */
    public ThreadLocalMetadataFactory(String url, String username, String password) {
        m_url = url;
        m_username = username;
        m_password = password;
    }

    @Override
    public DatabaseMetaData getMetadata() {
        return m_threadLocalMetadata.get();
    }

    /**
     * Closes all jdbc connections opened by this factory.
     * @throws IOException if a connection cannot be closed.
     */
    @Override
    public void close() throws IOException {
        for (Connection connection : m_connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }
}
