package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * InteractionComponent Class Description
 */
@Data
@NoArgsConstructor
public class InteractionComponent extends JSONBase {
    private String id;
    private LanguageMap description;

    public InteractionComponent(JsonNode jsonNode) {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(idNode.textValue());
        }

        JsonNode descriptionNode = jsonNode.path("description");
        if (! descriptionNode.isMissingNode()) {
            this.setDescription(new LanguageMap(descriptionNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.id != null) {
            node.put("id", this.getId());
        }
        if (this.description != null) {
            node.put("description", this.getDescription().toJSONNode(version));
        }

        return node;
    }
}
