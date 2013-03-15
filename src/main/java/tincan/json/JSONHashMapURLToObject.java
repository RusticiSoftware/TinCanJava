package tincan.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import tincan.TCAPIVersion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSONHashMapURLToObject Class Description
 */
@NoArgsConstructor
@Log
public class JSONHashMapURLToObject extends JSONHashMapBase {
    private final HashMap<URL,JsonNode> _map = new HashMap<URL,JsonNode>();

    protected JSONHashMapURLToObject(JsonNode jsonNode) throws MalformedURLException {
        super();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(new URL(item.getKey()), item.getValue());
        }
    }

    @Override
    protected HashMap _getMap() {
        return this._map;
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<URL,JsonNode> entry : this._map.entrySet()) {
            node.put(entry.getKey().toString(), entry.getValue());
        }

        return node;
    }

    public Object put(URL key, JsonNode val) {
        return this._map.put(key, val);
    }

    public Object put(String key, JsonNode val) throws MalformedURLException {
        return this.put(new URL(key), val);
    }

    public Object put(URL key, Object val) {
        JsonNode storeVal = Mapper.getInstance().valueToTree(val);
        return this.put(key, storeVal);
    }

    public Object put(URL key, StringOfJSON val) {
        JsonNode storeVal = Mapper.getInstance().valueToTree(val);
        return this.put(key, storeVal);
    }

    public Object put(String key, Object val) throws MalformedURLException {
        return this.put(new URL(key), val);
    }

    public JsonNode get(URL key) {
        return this._map.get(key);
    }

    public JsonNode get(String key) throws MalformedURLException {
        return this.get(new URL(key));
    }
}
