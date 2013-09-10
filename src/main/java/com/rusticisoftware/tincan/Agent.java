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
 * Agent model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Agent extends JSONBase implements QueryableStatementTarget {
    protected final String objectType = "Agent";
    private String name;
    private String mbox;
    private String mboxSHA1Sum;
    private String openID;
    private AgentAccount account;

    
    //MD - added a useful ctor
    public Agent(String _mbox)
    {
    	this.setMbox(_mbox);	
    }
    
    
    public static Agent fromJson(JsonNode jsonNode) {
        
        String objectType = "Agent";
        JsonNode objectTypeNode = jsonNode.path("objectType");
        if (! objectTypeNode.isMissingNode()) {
            objectType = objectTypeNode.textValue();
        }
        
        return "Group".equals(objectType) ? new Group(jsonNode) : new Agent(jsonNode);
    }
    
    protected Agent(JsonNode jsonNode) {
        this();

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(nameNode.textValue());
        }

        JsonNode mboxNode = jsonNode.path("mbox");
        if (! mboxNode.isMissingNode()) {
            this.setMbox(mboxNode.textValue());
        }

        JsonNode mboxSHA1SumNode = jsonNode.path("mbox_sha1sum");
        if (! mboxSHA1SumNode.isMissingNode()) {
            this.setMboxSHA1Sum(mboxSHA1SumNode.textValue());
        }

        JsonNode openIDNode = jsonNode.path("openid");
        if (! openIDNode.isMissingNode()) {
            this.setOpenID(openIDNode.textValue());
        }

        JsonNode acctNode = jsonNode.path("account");
        if (! acctNode.isMissingNode()) {
            this.setAccount(new AgentAccount(acctNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.name != null) {
            node.put("name", this.getName());
        }
        if (this.mbox != null) {
            node.put("mbox", this.getMbox());
        }
        if (this.mboxSHA1Sum != null) {
            node.put("mbox_sha1sum", this.getMboxSHA1Sum());
        }
        if (this.openID != null) {
            node.put("openid", this.getOpenID());
        }
        if (this.account != null) {
            node.put("account", this.getAccount().toJSONNode(version));
        }

        return node;
    }
}
