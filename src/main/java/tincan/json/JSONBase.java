package tincan.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    public String toJSON(TCAPIVersion version) throws IOException {
        return Mapper.getInstance().writeValueAsString(this.toJSONNode(version));
    }

    @Override
    public String toJSON() throws IOException {
        return this.toJSON(TCAPIVersion.latest());
    }

    @Override
    public String toJSONPretty(TCAPIVersion version) throws IOException {
        // TODO: check Jackson to see if this should be a different Writer
        ObjectMapper jsonMapper = Mapper.getInstance();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String result = jsonMapper.writeValueAsString(this.toJSONNode(version));

        jsonMapper.disable(SerializationFeature.INDENT_OUTPUT);

        return result;
    }

    @Override
    public String toJSONPretty() throws IOException {
        return this.toJSONPretty(TCAPIVersion.latest());
    }
}
