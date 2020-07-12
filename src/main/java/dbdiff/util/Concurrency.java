package dbdiff.util;

import dbdiff.pojos.error.InconsistentSchemaException;
import dbdiff.pojos.error.RelationalDatabaseReadException;

import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public enum Concurrency {;

    public static <T> void runInParallel(final ExecutorService executor
            , final Collection<? extends Callable<T>> tasks) throws RuntimeException {
        Collection<Future<T>> futures;
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        for (Future<T> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof SQLException) {
                    throw new RelationalDatabaseReadException(cause);
                } else {
                    throw new RuntimeException(e);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
