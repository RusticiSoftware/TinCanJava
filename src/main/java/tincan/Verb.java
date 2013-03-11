package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verb model class
 */
@Data
@NoArgsConstructor
public class Verb {
    private URL id;
    private LanguageMap display;

    public Verb(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URL(idNode.getTextValue()));
        }

        JsonNode displayNode = jsonNode.path("display");
        if (! displayNode.isMissingNode()) {
            this.setDisplay(new LanguageMap(displayNode));
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();
        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.display != null) {
            node.put("display", this.getDisplay().toJSONNode(version));
        }

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
