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
package com.rusticisoftware.tincan.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rusticisoftware.tincan.http.HTTPPart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.rusticisoftware.tincan.*;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;

/**
 * StatementBase Class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public abstract class StatementBase extends JSONBase {
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
    private List<Attachment> attachments;

    @Deprecated
    private Boolean voided;

    public StatementBase(JsonNode jsonNode) throws URISyntaxException, MalformedURLException, IOException, NoSuchAlgorithmException {
        this();

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
            if ("Group".equals(objectType) || "Agent".equals(objectType)) {
                this.setObject(Agent.fromJson(objectNode));
            }
            else if ("StatementRef".equals(objectType)) {
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
        
        JsonNode voidedNode = jsonNode.path("voided");
        if (! voidedNode.isMissingNode()) {
            this.setVoided(voidedNode.asBoolean());
        }
        
        JsonNode attachmentsNode = jsonNode.path("attachments");
        if (! attachmentsNode.isMissingNode()) {
            this.attachments = new ArrayList<Attachment>();
            for (JsonNode element : attachmentsNode) {
                this.attachments.add(new Attachment(element));
            }
        }
    }

    public StatementBase(StringOfJSON jsonStr) throws IOException, URISyntaxException, NoSuchAlgorithmException {
        this(jsonStr.toJSONNode());
    }

    public StatementBase(Agent actor, Verb verb, StatementTarget object, Result result, Context context) {
        this();

        this.setActor(actor);
        this.setVerb(verb);
        this.setObject(object);
        this.setResult(result);
        this.setContext(context);
    }

    public StatementBase(Agent actor, Verb verb, StatementTarget object) {
        this(actor, verb, object, null, null);
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();

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
        
        //Include 1.0.x specific fields if asking for 1.0.x version
        if (version.ordinal() <= TCAPIVersion.V100.ordinal()) {
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

    public boolean hasAttachments() {
        return (this.getAttachments() != null && this.getAttachments().size() > 0);
    }

    public boolean hasAttachmentsWithContent() {
        if (this.getAttachments() != null) {
            for (Attachment attachment : this.getAttachments()) {
                if (attachment.getContent().length > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addAttachment(Attachment attachment) {
        if (this.getAttachments() == null) {
            this.setAttachments(new ArrayList<Attachment>());
        }
        this.getAttachments().add(attachment);
    }

    public void addAttachments(Attachment attachments) {
        if (this.getAttachments() == null) {
            this.setAttachments(new ArrayList<Attachment>());
        }
        this.getAttachments().add(attachments);
    }

    public List<HTTPPart> getPartList() {
        List<HTTPPart> partList = new ArrayList<HTTPPart>();
        for (Attachment attachment : this.getAttachments()) {
            partList.add(attachment.getPart());
        }
        return partList;
    }
}
