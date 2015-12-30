/*
    Copyright 2015 Rustici Software

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Person Model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Person extends JSONBase {
    protected final String objectType = "Person";
    private List<String> name;
    private List<String> mbox;
    private List<String> mbox_sha1sum;
    private List<String> openid;
    private List<AgentAccount> account;

    public Person(StringOfJSON jsonStr) throws IOException {
        this(jsonStr.toJSONNode());
    }

    public Person(JsonNode jsonNode) {
        this();

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.name = new ArrayList<String>();

            for (JsonNode element : nameNode) {
                this.name.add(element.textValue());
            }
        }

        JsonNode mboxNode = jsonNode.path("mbox");
        if (! mboxNode.isMissingNode()) {
            this.mbox = new ArrayList<String>();

            for (JsonNode element : mboxNode) {
                this.mbox.add(element.textValue());
            }
        }

        JsonNode mbox_sha1sumNode = jsonNode.path("mbox_sha1sum");
        if (! mbox_sha1sumNode.isMissingNode()) {
            this.mbox_sha1sum = new ArrayList<String>();

            for (JsonNode element : mbox_sha1sumNode) {
                this.mbox_sha1sum.add(element.textValue());
            }
        }

        JsonNode openidNode = jsonNode.path("openid");
        if (! openidNode.isMissingNode()) {
            this.openid = new ArrayList<String>();

            for (JsonNode element : openidNode) {
                this.openid.add(element.textValue());
            }
        }

        JsonNode accountNode = jsonNode.path("account");
        if (! accountNode.isMissingNode()) {
            this.account = new ArrayList<AgentAccount>();

            for (JsonNode element : accountNode) {
                this.account.add(new AgentAccount(element));
            }
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();
        node.put("objectType", this.getObjectType());

        if (this.name != null && this.name.size() > 0) {
            ArrayNode name = mapper.createArrayNode();
            node.put("name", name);

            for (String element : this.getName()) {
                name.add(element);
            }
        }

        if (this.mbox != null && this.mbox.size() > 0) {
            ArrayNode mbox = mapper.createArrayNode();
            node.put("mbox", mbox);

            for (String element : this.getMbox()) {
                mbox.add(element);
            }
        }

        if (this.mbox_sha1sum != null && this.mbox_sha1sum.size() > 0) {
            ArrayNode mbox_sha1sum = mapper.createArrayNode();
            node.put("mbox_sha1sum", mbox_sha1sum);

            for (String element : this.getMbox_sha1sum()) {
                mbox_sha1sum.add(element);
            }
        }

        if (this.openid != null && this.openid.size() > 0) {
            ArrayNode openid = mapper.createArrayNode();
            node.put("openid", openid);

            for (String element : this.getOpenid()) {
                openid.add(element);
            }
        }

        if (this.account != null && this.account.size() > 0) {
            ArrayNode account = mapper.createArrayNode();
            node.put("account", account);

            for (AgentAccount element : this.getAccount()) {
                account.add(element.toJSONNode(version));
            }
        }

        return node;
    }
}
