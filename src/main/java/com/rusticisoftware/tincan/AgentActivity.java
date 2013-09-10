/*  Copyright 2013 Problem Solutions

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

/*
 * Added this class to the jar from Rustici for the SP2 project. 
 * 
 */


package com.rusticisoftware.tincan;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;


/**
 * Agent Activity model class -- 
 * MD - added to support things
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AgentActivity extends JSONBase implements QueryableStatementTarget
{ 
	
	private String objectType = "Agent";

	private String mbox;
    private String name;
    private String mboxSHA1Sum;
    private String openID;
    private AgentAccount account;
   
    
    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.mbox != null) {
            node.put("mbox", this.getMbox().toString());
        }if (this.name != null) {
            node.put("name", this.getName().toString());
        }if (this.mboxSHA1Sum != null) {
            node.put("mboxSHA1Sum", this.getMboxSHA1Sum().toString());
        }if (this.openID != null) {
            node.put("openID", this.getOpenID().toString());
        }
        
        return node;
    }
	
}

