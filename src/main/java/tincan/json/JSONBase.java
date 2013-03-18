package tincan.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import tincan.TCAPIVersion;

/**
 * JSONBase Class Description
 */
public abstract class JSONBase implements JSON {
    @Override
    public abstract ObjectNode toJSONNode(TCAPIVersion version);

    @Override
    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }

    @Override
    public String toJSON(TCAPIVersion version, Boolean pretty) throws IOException {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectWriter writer;
        if (pretty) {
            writer = mapper.writer().withDefaultPrettyPrinter();
        }
        else {
            writer = mapper.writer();
        }

        return writer.writeValueAsString(this.toJSONNode(version));
    }


    @Override
    public String toJSON(TCAPIVersion version) throws IOException {
        return this.toJSON(version, false);
    }

    @Override
    public String toJSON(Boolean pretty) throws IOException {
        return this.toJSON(TCAPIVersion.latest(), pretty);
    }

    @Override
    public String toJSON() throws IOException {
        return this.toJSON(TCAPIVersion.latest(), false);
    }
}
