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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.Mapper;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Group model class
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends Agent {
    protected final String objectType = "Group";
    private List<Agent> members;
    
    public Group() {
        super();
    }
    
    public Group(JsonNode jsonNode) {
        super(jsonNode);
        
        JsonNode memberNode = jsonNode.path("member");
        if (! memberNode.isMissingNode()) {
            this.members = new ArrayList<Agent>();
            Iterator it = memberNode.elements();
            while(it.hasNext()) {
                this.members.add(Agent.fromJson((JsonNode) it.next()));
            }
        }
    }
    
    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = super.toJSONNode(version);
        if (this.getMembers() != null && this.getMembers().size() > 0) {
            ArrayNode memberNode = Mapper.getInstance().createArrayNode();
            for (Agent member : this.getMembers()) {
                memberNode.add(member.toJSONNode(version));
            }
            node.put("member", memberNode);
        }
        return node;
    }
}
