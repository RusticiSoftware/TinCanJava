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

/**
 * Agent Account model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AgentAccount extends JSONBase {
    private String homePage;
    private String name;

    public AgentAccount(JsonNode jsonNode) {
        this();

        JsonNode homePageNode = jsonNode.path("homePage");
        if (! homePageNode.isMissingNode()) {
            this.setHomePage(homePageNode.textValue());
        }

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(nameNode.textValue());
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        if (this.homePage != null) {
            node.put("homePage", this.getHomePage());
        }
        if (this.name != null) {
            node.put("name", this.getName());
        }

        return node;
    }
}
