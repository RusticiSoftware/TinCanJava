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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public abstract class StatementBase extends JSONBase {
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
    private List<Attachment> attachments;

	public StatementBase() {
	}
    @Deprecated
    private Boolean voided;

    public StatementBase(JsonNode jsonNode) throws URISyntaxException, MalformedURLException {
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

    public StatementBase(StringOfJSON jsonStr) throws IOException, URISyntaxException {
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

        if (this.getResult() != null) {
            node.put("result", this.getResult().toJSONNode(version));
        }
        if (this.getContext() != null) {
            node.put("context", this.getContext().toJSONNode(version));
        }
        if (this.getTimestamp() != null) {
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

	public Agent getActor() {
		return actor;
	}

	public void setActor(Agent actor) {
		this.actor = actor;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public StatementTarget getObject() {
		return object;
	}

	public void setObject(StatementTarget object) {
		this.object = object;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Boolean getVoided() {
		return voided;
	}

	public void setVoided(Boolean voided) {
		this.voided = voided;
	}
}
