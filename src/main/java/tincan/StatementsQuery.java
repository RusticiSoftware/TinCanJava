package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Query model class used for building query parameters passed to get statements from LRS
 */
@Data
@NoArgsConstructor
public class StatementsQuery {
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

    public void setVerbID(String verbID) {
        this.verbID = verbID;
    }

    public void setVerbID(Verb verb) {
        this.setVerbID(verb.getId().toString());
    }

    public HashMap<String,String> toParameterMap(TCAPIVersion version) throws IOException {
        HashMap<String,String> params = new HashMap<String,String>();

        if (this.getVerbID() != null) {
            params.put("verb", this.getVerbID());
        }
        if (this.getObject() != null) {
            params.put("object", this.getObject().toJSON(version));
        }
        if (this.getRegistration() != null) {
            params.put("registration", this.getRegistration().toString());
        }
        if (this.getContext() != null) {
            params.put("context", this.getContext().toString());
        }
        if (this.getActor() != null) {
            params.put("actor", this.getActor().toJSON(version));
        }
        if (this.getSince() != null) {
            //
            // The following was giving ZZ which has a : in it which was blowing up
            //params.put("since", this.getSince().toString(ISODateTimeFormat.dateTime()));
            //
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            params.put("since", this.getSince().toString(fmt));
        }
        if (this.getUntil() != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            params.put("until", this.getUntil().toString(fmt));
        }
        if (this.getLimit() != null) {
            params.put("limit", this.getLimit().toString());
        }
        if (this.getAuthoritative() != null) {
            params.put("authoritative", this.getAuthoritative().toString());
        }
        if (this.getSparse() != null) {
            params.put("sparse", this.getSparse().toString());
        }
        if (this.getInstructor() != null) {
            params.put("instructor", this.getInstructor().toJSON(version));
        }
        if (this.getAscending() != null) {
            params.put("ascending", this.getAscending().toString());
        }

        return params;
    }
}
