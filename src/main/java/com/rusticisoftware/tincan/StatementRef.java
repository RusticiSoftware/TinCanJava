package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

import java.util.UUID;

/**
 * StatementRef Class used when referencing another statement from a statement's
 * object property
 */
@Data
public class StatementRef extends JSONBase implements StatementTarget {
    private final String objectType = "StatementRef";
    private UUID id;

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        node.put("id", this.getId().toString());

        return null;
    }
}
