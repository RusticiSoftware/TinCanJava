package tincan.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * StringOfJSON Class Description
 */
public class StringOfJSON {
    private String source;

    public StringOfJSON(String json) {
        this.source = json;
    }

    public JsonNode toJSONNode() throws IOException {
        if (this.source == null) {
            return null;
        }
        return Mapper.getInstance().readValue(this.source, JsonNode.class);
    }

    @Override
    public String toString() {
        return this.source;
    }
}
