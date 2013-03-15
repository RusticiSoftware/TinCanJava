package tincan;

import java.io.IOException;

/**
 * Essentially empty interface to force type checking for objects of a statement that are queryable
 */
public interface QueryableStatementTarget extends StatementTarget {
    String toJSON(TCAPIVersion version) throws IOException;
}
