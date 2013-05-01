/*
    Copyright 2013 Rustici Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * StatementRef Class used when referencing another statement from a statement's
 * object property
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatementRef extends JSONBase implements StatementTarget {
    private final String objectType = "StatementRef";
    private UUID id;

    public StatementRef() {
    }
    
    public StatementRef (UUID id) {
        this.id = id;
    }
    
    public StatementRef(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(UUID.fromString(idNode.textValue()));
        }
    }
    
    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.objectType);
        node.put("id", this.getId().toString());
        return node;
    }
}
