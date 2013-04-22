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
 * Verb model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Verb extends JSONBase {
    private URL id;
    private LanguageMap display;

    public Verb(URL id) {
        this.id = id;
    }

    public Verb(URL id, String display) {
        this(id);

        LanguageMap displayMap = new LanguageMap();
        displayMap.put("und", display);
        this.setDisplay(displayMap);
    }

    public Verb(String id) throws MalformedURLException {
        this(new URL(id));
    }

    public Verb(String id, String display) throws MalformedURLException {
        this(new URL(id), display);
    }

    public Verb(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(new URL(idNode.textValue()));
        }

        JsonNode displayNode = jsonNode.path("display");
        if (! displayNode.isMissingNode()) {
            this.setDisplay(new LanguageMap(displayNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.display != null) {
            node.put("display", this.getDisplay().toJSONNode(version));
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
