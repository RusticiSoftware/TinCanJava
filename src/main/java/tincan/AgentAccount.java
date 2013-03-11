package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Agent Account model class
 */
@Data
@NoArgsConstructor
public class AgentAccount {
    private String homePage;
    private String name;

    public AgentAccount(JsonNode jsonNode) {
        this();

        JsonNode homePageNode = jsonNode.path("homePage");
        if (! homePageNode.isMissingNode()) {
            this.setHomePage(homePageNode.getTextValue());
        }

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(nameNode.getTextValue());
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();
        if (this.homePage != null) {
            node.put("homePage", this.getHomePage());
        }
        if (this.name != null) {
            node.put("name", this.getName());
        }

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
