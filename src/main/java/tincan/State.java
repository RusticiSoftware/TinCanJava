package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import tincan.json.StringOfJSON;

import java.io.IOException;

/**
 * State Class
 */
@Data
@NoArgsConstructor
public class State {
    // TODO: need SHA1 of the contents?
    private String id;
    private DateTime updated;
    private JsonNode contents;

    public State(String id, JsonNode jsonNode) {
        this.setId(id);
        this.setContents(jsonNode);
    }

    public State(String id, StringOfJSON jsonStr) throws IOException {
        this(id, jsonStr.toJSONNode());
    }
}
