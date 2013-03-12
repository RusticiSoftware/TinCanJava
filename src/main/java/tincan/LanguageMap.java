package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import java.util.Iterator;
import java.util.Map;
import tincan.json.JSONHashMapStringToString;

/**
 * Language map
 */
@NoArgsConstructor
public class LanguageMap extends JSONHashMapStringToString {
    public LanguageMap(JsonNode jsonNode) {
        this();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(item.getKey(), item.getValue().textValue());
        }
    }
}
