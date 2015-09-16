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
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * InteractionComponent Class Description
 */
public class InteractionComponent extends JSONBase {
    private String id;
    private LanguageMap description;

	public InteractionComponent() {
	}
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
        if (this.getId() != null) {
            node.put("id", this.getId());
        }
        if (this.getDescription() != null) {
            node.put("description", this.getDescription().toJSONNode(version));
        }

        return node;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LanguageMap getDescription() {
		return description;
	}

	public void setDescription(LanguageMap description) {
		this.description = description;
	}
}
