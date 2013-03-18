package tincan;

import java.util.List;
import java.util.UUID;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement retrieveStatement(String id) throws Exception;
    StatementsResult queryStatements(StatementsQuery query) throws Exception;
    StatementsResult moreStatements(String moreURL) throws Exception;
    UUID saveStatement(Statement statement) throws Exception;
    List<String> saveStatements(List<Statement> statements) throws Exception;

    State retrieveState(String id, String activityId, Agent agent, UUID registration) throws Exception;
    void saveState(State state, String activityId, Agent agent, UUID registration) throws Exception;
}
