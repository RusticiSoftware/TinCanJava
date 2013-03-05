package tincan;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement getStatement(String id);
    StatementsResult getStatements(StatementsQuery query);
    void putStatement(Statement statement);
    void putStatements(Statement[] statements);
}
