package tincan;

import lombok.Data;
import org.joda.time.DateTime;
import java.util.UUID;

/**
 * Query model class used for building query parameters passed to get statements from LRS
 */
@Data
public class StatementsQuery {
    private Verb verb;
    private String verbID;
    private StatementTarget object;
    private String objectID;
    private UUID registration;
    private Boolean context;
    private Agent actor;
    private DateTime since;
    private DateTime until;
    private Integer limit = 0;
    private Boolean authoritative;
    private Boolean sparse;
    private Agent instructor;
    private Boolean ascending;
    //private String continueToken;
}
