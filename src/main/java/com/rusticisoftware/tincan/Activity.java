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
import lombok.NoArgsConstructor;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Activity model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Activity extends JSONBase implements QueryableStatementTarget {
    private final String objectType = "Activity";

    private URL id;
    private ActivityDefinition definition;

    public Activity(URL id) {
        this.id = id;
    }

    public Activity(String id) throws MalformedURLException {
        this(new URL(id));
    }

    public Activity(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URL(idNode.textValue()));
        }

        JsonNode definitionNode = jsonNode.path("definition");
        if (! definitionNode.isMissingNode()) {
            this.setDefinition(new ActivityDefinition(definitionNode));
        }
    }

    public Activity(String id, String name, String description) throws MalformedURLException {
        this(id);

        this.setDefinition(new ActivityDefinition(name, description));
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.definition != null) {
            node.put("definition", this.getDefinition().toJSONNode());
        }

        return node;
    }

    public void setId(URL id) {
        this.id = id;
    }

    public void setId(String id) throws MalformedURLException {
        this.setId(new URL(id));
    }
}
