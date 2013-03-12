package tincan.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import tincan.TCAPIVersion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSONHashMapStringToString Class Description
 */
@NoArgsConstructor
public class JSONHashMapStringToString extends JSONHashMapBase {
    private final HashMap<String,String> _map = new HashMap<String, String>();

    public JSONHashMapStringToString(JsonNode jsonNode) {
        super();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(item.getKey(), item.getValue().textValue());
        }
    }

    @Override
    protected HashMap _getMap() {
        return this._map;
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<String, String> entry : this._map.entrySet()) {
            node.put(entry.getKey(), entry.getValue());
        }

        return node;
    }

    public String put(String key, String val) {
        return this._map.put(key, val);
    }

    public String get(String key) {
        return this._map.get(key);
    }
}
