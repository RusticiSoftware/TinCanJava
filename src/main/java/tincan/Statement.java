package tincan;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Statement Class
 */
@Data
public class Statement {
    private UUID id;
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
    private DateTime stored;
    private Agent authority;
    private Boolean voided;
    private Boolean inProgress;
    private String _json;

}
