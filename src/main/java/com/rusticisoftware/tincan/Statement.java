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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;

/**
 * Statement Class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Statement extends JSONBase {
    private UUID id;
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
    private DateTime stored;
    private Agent authority;
    private TCAPIVersion version;
    private List<Attachment> attachments;
    
    
    @Deprecated
    private Boolean voided;

    public Statement(JsonNode jsonNode) throws URISyntaxException, MalformedURLException {
        this();

        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(UUID.fromString(idNode.textValue()));
        }

        JsonNode actorNode = jsonNode.path("actor");
        if (! actorNode.isMissingNode()) {
            this.setActor(Agent.fromJson(actorNode));
        }

        JsonNode verbNode = jsonNode.path("verb");
        if (! verbNode.isMissingNode()) {
            this.setVerb(new Verb(verbNode));
        }

        JsonNode objectNode = jsonNode.path("object");
        if (! objectNode.isMissingNode()) {
            String objectType = objectNode.path("objectType").textValue();
            if ("Group".equals(objectType) || "Agent".equals(objectType)){
                this.setObject(Agent.fromJson(objectNode));
            }
            else if ("StatementRef".equals(objectType)){
                this.setObject(new StatementRef(objectNode));
            }
            else if ("SubStatement".equals(objectType)) {
                this.setObject(new SubStatement(objectNode));
            }
            else {
                this.setObject(new Activity(objectNode));
            }
        }

        JsonNode resultNode = jsonNode.path("result");
        if (! resultNode.isMissingNode()) {
            this.setResult(new Result(resultNode));
        }

        JsonNode contextNode = jsonNode.path("context");
        if (! contextNode.isMissingNode()) {
            this.setContext(new Context(contextNode));
        }

        JsonNode timestampNode = jsonNode.path("timestamp");
        if (! timestampNode.isMissingNode()) {
            this.setTimestamp(new DateTime(timestampNode.textValue()));
        }

        JsonNode storedNode = jsonNode.path("stored");
        if (! storedNode.isMissingNode()) {
            this.setStored(new DateTime(storedNode.textValue()));
        }

        JsonNode authorityNode = jsonNode.path("authority");
        if (! authorityNode.isMissingNode()) {
            this.setAuthority(Agent.fromJson(authorityNode));
        }
        
        JsonNode voidedNode = jsonNode.path("voided");
        if (! voidedNode.isMissingNode()) {
            this.setVoided(voidedNode.asBoolean());
        }
        
        JsonNode versionNode = jsonNode.path("version");
        if (! versionNode.isMissingNode()) {
            this.setVersion(TCAPIVersion.fromString(versionNode.textValue()));
        }
        
        JsonNode attachmentsNode = jsonNode.path("attachments");
        if (! attachmentsNode.isMissingNode()) {
            this.attachments = new ArrayList<Attachment>();
            for (JsonNode element : attachmentsNode) {
                this.attachments.add(new Attachment(element));
            }
        }
    }

    public Statement(StringOfJSON jsonStr) throws IOException, URISyntaxException {
        this(jsonStr.toJSONNode());
    }

    public Statement(Agent actor, Verb verb, StatementTarget object, Result result, Context context) {
        this();

        this.setActor(actor);
        this.setVerb(verb);
        this.setObject(object);
        this.setResult(result);
        this.setContext(context);
    }

    public Statement(Agent actor, Verb verb, StatementTarget object) {
        this(actor, verb, object, null, null);
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();

        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        node.put("actor", this.getActor().toJSONNode(version));
        node.put("verb", this.getVerb().toJSONNode(version));
        node.put("object", this.getObject().toJSONNode(version));

        if (this.result != null) {
            node.put("result", this.getResult().toJSONNode(version));
        }
        if (this.context != null) {
            node.put("context", this.getContext().toJSONNode(version));
        }
        if (this.timestamp != null) {
            node.put("timestamp", fmt.print(this.getTimestamp()));
        }
        if (this.stored != null) {
            node.put("stored", fmt.print(this.getStored()));
        }
        if (this.authority != null) {
            node.put("authority", this.getAuthority().toJSONNode(version));
        }
        
        //Include 0.95 specific fields if asking for 0.95 version
        if (TCAPIVersion.V095.equals(version)) {
            if (this.getVoided() != null) {
                node.put("voided", this.getVoided());
            }
        }
        
        //Include 1.0.x specific fields if asking for 1.0.x version
        if (version.ordinal() <= TCAPIVersion.V100.ordinal()) {
            if (this.getVersion() != null) {
                node.put("version", this.getVersion().toString());
            }
            if (this.getAttachments() != null && this.getAttachments().size() > 0) {
                ArrayNode attachmentsNode = Mapper.getInstance().createArrayNode();
                for (Attachment attachment : this.getAttachments()) {
                    attachmentsNode.add(attachment.toJSONNode(version));
                }
                node.put("attachments", attachmentsNode);
            }
        }
        
        return node;
    }

    /**
     * Method to set a random ID and the current date/time in the 'timestamp'
     */
    public void stamp() {
        if (this.id == null) {
            this.setId(UUID.randomUUID());
        }
        if (this.timestamp == null) {
            this.setTimestamp(new DateTime());
        }
    }
}
