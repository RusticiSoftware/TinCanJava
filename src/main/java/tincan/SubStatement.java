package tincan;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * SubStatement Class used when including a statement like object in another statement,
 * see the 'statement' context property
 */
@Data
public class SubStatement {
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
}
