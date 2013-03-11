package tincan;

import java.io.IOException;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement getStatement(String id) throws Exception;
    StatementsResult getStatements(StatementsQuery query) throws Exception;
    void putStatement(Statement statement) throws Exception;
    void putStatements(Statement[] statements) throws Exception;
}
