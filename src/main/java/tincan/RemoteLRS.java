package tincan;

import lombok.Data;
import java.util.HashMap;

/**
 * Class used to communicate with a TCAPI endpoint
 */
@Data
public class RemoteLRS implements LRS {
    private String endpoint;
    private String version;
    private String auth;
    private HashMap extended;

    @Override
    public Statement getStatement(String id) {
        return null;
    }

    @Override
    public StatementsResult getStatements(StatementsQuery query) {
        return null;
    }

    @Override
    public void putStatement(Statement statement) {
    }

    @Override
    public void putStatements(Statement[] statements) {
    }
}
