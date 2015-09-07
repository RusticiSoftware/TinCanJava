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

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Activity model class
 */
public class Activity extends JSONBase implements QueryableStatementTarget {
    private String objectType = "Activity";

    private URI id;
    private ActivityDefinition definition;

    public Activity() {
    }

    public Activity(URI id) {
        this.id = id;
    }

    public Activity(String id) throws URISyntaxException {
        this(new URI(id));
    }

    public Activity(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URI(idNode.textValue()));
        }

        JsonNode definitionNode = jsonNode.path("definition");
        if (! definitionNode.isMissingNode()) {
            this.setDefinition(new ActivityDefinition(definitionNode));
        }
    }

    public Activity(String id, String name, String description) throws URISyntaxException {
        this(id);

        this.setDefinition(new ActivityDefinition(name, description));
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.getId() != null) {
            node.put("id", this.getId().toString());
        }
        if (this.getDefinition() != null) {
            node.put("definition", this.getDefinition().toJSONNode());
        }

        return node;
    }

    public void setId(URI id) {
        this.id = id;
    }

    public void setId(String id) throws URISyntaxException {
        this.setId(new URI(id));
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public URI getId() {
        return id;
    }

    public ActivityDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ActivityDefinition definition) {
        this.definition = definition;
    }
}
