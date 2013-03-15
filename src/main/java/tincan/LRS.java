package tincan;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement fetchStatement(String id) throws Exception;
    StatementsResult queryStatements(StatementsQuery query) throws Exception;
    void saveStatement(Statement statement) throws Exception;
    void saveStatements(Statement[] statements) throws Exception;
}
