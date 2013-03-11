package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Agent model class
 */
@Data
@NoArgsConstructor
public class Agent implements StatementTarget {
    private final String objectType = "Agent";
    private String name;
    private String mbox;
    private String mboxSHA1Sum;
    private String openID;
    private AgentAccount account;

    public Agent(JsonNode jsonNode) {
        this();

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(nameNode.getTextValue());
        }

        JsonNode mboxNode = jsonNode.path("mbox");
        if (! mboxNode.isMissingNode()) {
            this.setMbox(mboxNode.getTextValue());
        }

        JsonNode acctNode = jsonNode.path("account");
        if (! acctNode.isMissingNode()) {
            this.setAccount(new AgentAccount(acctNode));
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.name != null) {
            node.put("name", this.getName());
        }
        if (this.mbox != null) {
            node.put("mbox", this.getMbox());
        }
        if (this.account != null) {
            node.put("account", this.getAccount().toJSONNode(version));
        }

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
