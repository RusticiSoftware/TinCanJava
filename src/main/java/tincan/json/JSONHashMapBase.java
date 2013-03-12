package tincan.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import tincan.TCAPIVersion;

/**
 * JSONHashMapBase Class Description
 */
public abstract class JSONHashMapBase extends JSONBase {
    protected abstract HashMap _getMap();
    public abstract ObjectNode toJSONNode(TCAPIVersion version);
}
