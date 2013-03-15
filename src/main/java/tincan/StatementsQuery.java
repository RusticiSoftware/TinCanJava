package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import java.util.UUID;

/**
 * Query model class used for building query parameters passed to get statements from LRS
 */
@Data
@NoArgsConstructor
public class StatementsQuery {
    private Verb verb;
    private String verbID;
    private QueryableStatementTarget object;
    private UUID registration;
    private Boolean context;
    private Agent actor;
    private DateTime since;
    private DateTime until;
    private Integer limit;
    private Boolean authoritative;
    private Boolean sparse;
    private Agent instructor;
    private Boolean ascending;
    //private String continueToken;
}
