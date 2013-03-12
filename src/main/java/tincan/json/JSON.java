package tincan.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tincan.TCAPIVersion;

import java.io.IOException;

/**
 * Interface to implement to provide JSON support for nesting of objects
 */
public interface JSON {
    public ObjectNode toJSONNode(TCAPIVersion version);
    public ObjectNode toJSONNode();
    public String toJSON() throws IOException;
    public String toJSONPretty() throws IOException;
}
