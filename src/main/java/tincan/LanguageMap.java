package tincan;

import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Language map
 */
@NoArgsConstructor
public class LanguageMap extends HashMap<String, String> {
    public LanguageMap(JsonNode jsonNode) {
        this();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.getFields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(item.getKey(), item.getValue().getTextValue());
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();

        for (Map.Entry<String, String> entry : this.entrySet()) {
            node.put(entry.getKey(), entry.getValue());
        }

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
