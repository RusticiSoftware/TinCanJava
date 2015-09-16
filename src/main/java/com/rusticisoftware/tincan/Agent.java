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
 * Agent model class
 */
public class Agent extends JSONBase implements QueryableStatementTarget {
    protected String objectType = "Agent";
    protected String name;
    protected String mbox;
    protected String mboxSHA1Sum;
    protected String openID;
    protected AgentAccount account;

    public Agent() {
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

        if (this.getName() != null) {
            node.put("name", this.getName());
        }
        if (this.getMbox() != null) {
            node.put("mbox", this.getMbox());
        }
        if (this.getMboxSHA1Sum() != null) {
            node.put("mbox_sha1sum", this.getMboxSHA1Sum());
        }
        if (this.getOpenID() != null) {
            node.put("openid", this.getOpenID());
        }
        if (this.getAccount() != null) {
            node.put("account", this.getAccount().toJSONNode(version));
        }

        return node;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbox() {
        return mbox;
    }

    public void setMbox(String mbox) {
        this.mbox = mbox;
    }

    public String getMboxSHA1Sum() {
        return mboxSHA1Sum;
    }

    public void setMboxSHA1Sum(String mboxSHA1Sum) {
        this.mboxSHA1Sum = mboxSHA1Sum;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public AgentAccount getAccount() {
        return account;
    }

    public void setAccount(AgentAccount account) {
        this.account = account;
    }
}
