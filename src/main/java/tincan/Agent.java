package tincan;

import lombok.Data;

/**
 * Agent model class
 */
@Data
public class Agent implements StatementTarget {
    private final String objectType = "Agent";
    private String name;
    private String mbox;
    private String mboxSHA1Sum;
    private String openID;
    private AgentAccount account;
}
