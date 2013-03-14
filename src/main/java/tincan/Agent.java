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

        JsonNode mboxSHA1SumNode = jsonNode.path("mboxSHA1Sum");
        if (! mboxSHA1SumNode.isMissingNode()) {
            this.setMboxSHA1Sum(mboxSHA1SumNode.textValue());
        }

        JsonNode openIDNode = jsonNode.path("openID");
        if (! openIDNode.isMissingNode()) {
            this.setOpenID(openIDNode.textValue());
        }

        JsonNode acctNode = jsonNode.path("account");
        if (! acctNode.isMissingNode()) {
            this.setAccount(new AgentAccount(acctNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.name != null) {
            node.put("name", this.getName());
        }
        if (this.mbox != null) {
            node.put("mbox", this.getMbox());
        }
        if (this.mboxSHA1Sum != null) {
            node.put("mbox_sha1sum", this.getMboxSHA1Sum());
        }
        if (this.openID != null) {
            node.put("openid", this.getOpenID());
        }
        if (this.account != null) {
            node.put("account", this.getAccount().toJSONNode(version));
        }

        return node;
    }
}
