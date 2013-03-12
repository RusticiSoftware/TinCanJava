package tincan.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tincan.TCAPIVersion;

import java.util.HashMap;
import java.util.Map;

/**
 * JSONHashMapStringToObject Class Description
 */
public class JSONHashMapStringToObject extends JSONHashMapBase {
    private final HashMap<String,Object> _map = new HashMap<String, Object>();

    @Override
    protected HashMap _getMap() {
        return this._map;
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<String, Object> entry : this._map.entrySet()) {
            node.put(entry.getKey(), ((JSONBase) entry.getValue()).toJSONNode(version));
        }

        return node;
    }

    public Object put(String key, Object val) {
        return this._map.put(key, val);
    }

    public Object get(String key) {
        return this._map.get(key);
    }
}
