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

import java.net.URISyntaxException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * SubStatement Class used when including a statement like object in another statement,
 * see the 'statement' context property
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SubStatement extends JSONBase implements StatementTarget {
    private final String objectType = "SubStatement";

    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;

    public SubStatement (JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode actorNode = jsonNode.path("actor");
        if (! actorNode.isMissingNode()) {
            this.setActor(Agent.fromJson(actorNode));
        }

        JsonNode verbNode = jsonNode.path("verb");
        if (! verbNode.isMissingNode()) {
            this.setVerb(new Verb(verbNode));
        }

        JsonNode objectNode = jsonNode.path("object");
        if (! objectNode.isMissingNode()) {
            JsonNode objectTypeNode = objectNode.path("objectType");
            if (objectTypeNode.textValue().equals("Activity")) {
                this.setObject(new Activity(objectNode));
            }
        }
    }

    public SubStatement (String json) throws Exception {
        this(Mapper.getInstance().readValue(json, JsonNode.class));
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        node.put("actor", this.getActor().toJSONNode(version));
        node.put("verb", this.getVerb().toJSONNode(version));
        node.put("object", this.getObject().toJSONNode(version));

        return node;
    }
}
