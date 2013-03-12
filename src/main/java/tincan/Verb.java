package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Verb model class
 */
@Data
@NoArgsConstructor
public class Verb extends JSONBase {
    private URL id;
    private LanguageMap display;

    public Verb(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URL(idNode.textValue()));
        }

        JsonNode displayNode = jsonNode.path("display");
        if (! displayNode.isMissingNode()) {
            this.setDisplay(new LanguageMap(displayNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.display != null) {
            node.put("display", this.getDisplay().toJSONNode(version));
        }

        return node;
    }
}
