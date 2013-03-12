package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;

/**
 * Activity Definition model class
 */
@Data
@NoArgsConstructor
public class ActivityDefinition extends JSONBase {
    private LanguageMap name;
    private LanguageMap description;
    private String type;
    private Extensions extensions;
    private InteractionType interactionType;
    private String[] correctResponsesPattern;
    private InteractionComponent[] choices;
    private InteractionComponent[] scale;
    private InteractionComponent[] source;
    private InteractionComponent[] target;
    private InteractionComponent[] steps;

    public ActivityDefinition(JsonNode jsonNode) {
        this();

        JsonNode typeNode = jsonNode.path("type");
        if (! typeNode.isMissingNode()) {
            this.setType(typeNode.textValue());
        }

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(new LanguageMap(nameNode));
        }

        JsonNode descNode = jsonNode.path("description");
        if (! descNode.isMissingNode()) {
            this.setDescription(new LanguageMap(descNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.type != null) {
            node.put("type", this.getType());
        }
        if (this.name != null) {
            node.put("name", this.getName().toJSONNode(version));
        }
        if (this.description != null) {
            node.put("description", this.getDescription().toJSONNode(version));
        }

        return node;
    }
}
