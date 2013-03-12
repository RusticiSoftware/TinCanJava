package tincan.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tincan.TCAPIVersion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * JSONHashMapURLToObject Class Description
 */
public class JSONHashMapURLToObject extends JSONHashMapBase {
    private final HashMap<URL,Object> _map = new HashMap<URL, Object>();

    @Override
    protected HashMap _getMap() {
        return this._map;
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<URL, Object> entry : this._map.entrySet()) {
            node.put(entry.getKey().toString(), ((JSONBase) entry.getValue()).toJSONNode(version));
        }

        return node;
    }

    public Object put(URL key, Object val) {
        return this._map.put(key, val);
    }

    public Object put(String key, Object val) throws MalformedURLException {
        return this.put(new URL(key), val);
    }

    public Object get(URL key) {
        return this._map.get(key);
    }

    public Object get(String key) throws MalformedURLException {
        return this.get(new URL(key));
    }
}
