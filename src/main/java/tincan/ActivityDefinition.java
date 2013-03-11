package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Activity Definition model class
 */
@Data
@NoArgsConstructor
public class ActivityDefinition {
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
            this.setType(typeNode.getTextValue());
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

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = JSONMapper.getInstance().createObjectNode();
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

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }
}
