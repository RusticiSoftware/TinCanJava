package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;
import java.net.MalformedURLException;

/**
 * ContextActivities Model class
 */
@Data
@NoArgsConstructor
public class ContextActivities extends JSONBase {
    private Activity parent;
    private Activity grouping;
    private Activity other;

    public ContextActivities(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode parentNode = jsonNode.path("parent");
        if (! parentNode.isMissingNode()) {
            this.setParent(new Activity(parentNode));
        }

        JsonNode groupingNode = jsonNode.path("grouping");
        if (! groupingNode.isMissingNode()) {
            this.setGrouping(new Activity(groupingNode));
        }

        JsonNode otherNode = jsonNode.path("other");
        if (! otherNode.isMissingNode()) {
            this.setOther(new Activity(otherNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();

        if (this.parent != null) {
            node.put("parent", this.getParent().toJSONNode(version));
        }
        if (this.grouping != null) {
            node.put("grouping", this.getGrouping().toJSONNode(version));
        }
        if (this.other != null) {
            node.put("other", this.getOther().toJSONNode(version));
        }

        return node;
    }
}
