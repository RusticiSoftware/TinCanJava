package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity model class
 */
@Data
@NoArgsConstructor
public class Activity implements StatementTarget {
    private final String objectType = "Activity";

    private URL id;
    private ActivityDefinition definition;

    public Activity(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URL(idNode.getTextValue()));
        }

        JsonNode definitionNode = jsonNode.path("definition");
        if (! definitionNode.isMissingNode()) {
            this.setDefinition(new ActivityDefinition(definitionNode));
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.definition != null) {
            node.put("definition", this.getDefinition().toJSONNode());
        }

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
