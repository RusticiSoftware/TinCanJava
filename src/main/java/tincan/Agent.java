package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;

/**
 * Agent model class
 */
@Data
@NoArgsConstructor
public class Agent extends JSONBase implements StatementTarget {
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
            this.setName(nameNode.textValue());
        }

        JsonNode mboxNode = jsonNode.path("mbox");
        if (! mboxNode.isMissingNode()) {
            this.setMbox(mboxNode.textValue());
        }

        JsonNode acctNode = jsonNode.path("account");
        if (! acctNode.isMissingNode()) {
            this.setAccount(new AgentAccount(acctNode));
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
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
