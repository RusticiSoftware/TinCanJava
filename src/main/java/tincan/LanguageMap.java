package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import tincan.json.Mapper;

/**
 * Language map
 */
@NoArgsConstructor
public class LanguageMap {
    private final HashMap<String,String> _map = new HashMap<String, String>();

    public LanguageMap(JsonNode jsonNode) {
        this();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(item.getKey(), item.getValue().textValue());
        }
    }

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
