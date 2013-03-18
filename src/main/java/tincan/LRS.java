package tincan;

import java.util.UUID;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement retrieveStatement(String id) throws Exception;
    StatementsResult queryStatements(StatementsQuery query) throws Exception;
    StatementsResult moreStatements(String moreURL) throws Exception;
    UUID saveStatement(Statement statement) throws Exception;
    void saveStatements(Statement[] statements) throws Exception;

    State retrieveState(String id, String activityId, Agent agent, UUID registration) throws Exception;
    State retrieveState(String id, Activity activity, Agent agent, UUID registration) throws Exception;
    void saveState(State state, String activityId, Agent agent, UUID registration) throws Exception;
    void saveState(State state, Activity activity, Agent agent, UUID registration) throws Exception;
}
