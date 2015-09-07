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
 * Verb model class
 */
public class Verb extends JSONBase {
    private URI id;
    private LanguageMap display;

	public Verb() {
	}
	

    public Verb(URI id) {
        this.id = id;
    }

    public Verb(URI id, String display) {
        this(id);

        LanguageMap displayMap = new LanguageMap();
        displayMap.put("und", display);
        this.setDisplay(displayMap);
    }

    public Verb(String id) throws URISyntaxException {
        this(new URI(id));
    }

    public Verb(String id, String display) throws URISyntaxException {
        this(new URI(id), display);
    }

    public Verb(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URI(idNode.textValue()));
        }

        JsonNode displayNode = jsonNode.path("display");
        if (! displayNode.isMissingNode()) {
            this.setDisplay(new LanguageMap(displayNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.getId() != null) {
            node.put("id", this.getId().toString());
        }
        if (this.getDisplay() != null) {
            node.put("display", this.getDisplay().toJSONNode(version));
        }

        return node;
    }

    public void setId(URI id) {
        this.id = id;
    }

    public void setId(String id) throws URISyntaxException {
        this.setId(new URI(id));
    }

	public URI getId() {
		return id;
	}

	public LanguageMap getDisplay() {
		return display;
	}

	public void setDisplay(LanguageMap display) {
		this.display = display;
	}
}
