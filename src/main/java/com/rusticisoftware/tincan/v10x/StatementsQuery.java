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
package com.rusticisoftware.tincan.v10x;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.QueryResultFormat;
import com.rusticisoftware.tincan.QueryableStatementTarget;
import com.rusticisoftware.tincan.StatementsQueryInterface;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Query model class used for building query parameters passed to get statements from LRS
 */
public class StatementsQuery implements StatementsQueryInterface {
    private TCAPIVersion version = TCAPIVersion.V100;
    private Agent agent;
    private URI verbID;
    private URI activityID;
    private UUID registration;
    private Boolean relatedActivities;
    private Boolean relatedAgents;
    private DateTime since;
    private DateTime until;
    private Integer limit;
    private QueryResultFormat format;
    //TODO: Expose when attachments are supported here
    //private Boolean attachments;
    private Boolean ascending;

	public StatementsQuery() {
	}
	

    public void setVerbID(String verbID) throws URISyntaxException {
		this.setVerbID(new URI(verbID));
    }

    public void setVerbID(Verb verb) throws URISyntaxException {
        this.setVerbID(verb.getId().toString());
    }

    public HashMap<String,String> toParameterMap() throws IOException {
        HashMap<String,String> params = new HashMap<String,String>();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();

        if (this.getAgent() != null) {
            params.put("agent", this.getAgent().toJSON(getVersion()));
        }
        if (this.getVerbID() != null) {
            params.put("verb", this.getVerbID().toString());
        }
        if (this.getActivityID() != null) {
            params.put("activity", this.getActivityID().toString());
        }
        if (this.getRegistration() != null) {
            params.put("registration", this.getRegistration().toString());
        }
        if (this.getRelatedActivities() != null) {
            params.put("related_activities", this.getRelatedActivities().toString());
        }
        if (this.getRelatedAgents() != null) {
            params.put("related_agents", this.getRelatedAgents().toString());
        }
        if (this.getSince() != null) {
            params.put("since", fmt.print(this.getSince()));
        }
        if (this.getUntil() != null) {
            params.put("until", fmt.print(this.getUntil()));
        }
        if (this.getLimit() != null) {
            params.put("limit", this.getLimit().toString());
        }
        if (this.getFormat() != null) {
            params.put("format", this.getFormat().toString().toLowerCase());
        }
        if (this.getAscending() != null) {
            params.put("ascending", this.getAscending().toString());
        }

        return params;
    }

	public TCAPIVersion getVersion() {
		return version;
	}

	public void setVersion(TCAPIVersion version) {
		this.version = version;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public URI getVerbID() {
		return verbID;
	}

	public void setVerbID(URI verbID) {
		this.verbID = verbID;
	}

	public URI getActivityID() {
		return activityID;
	}

	public void setActivityID(URI activityID) {
		this.activityID = activityID;
	}

	public UUID getRegistration() {
		return registration;
	}

	public void setRegistration(UUID registration) {
		this.registration = registration;
	}

	public Boolean getRelatedActivities() {
		return relatedActivities;
	}

	public void setRelatedActivities(Boolean relatedActivities) {
		this.relatedActivities = relatedActivities;
	}

	public Boolean getRelatedAgents() {
		return relatedAgents;
	}

	public void setRelatedAgents(Boolean relatedAgents) {
		this.relatedAgents = relatedAgents;
	}

	public DateTime getSince() {
		return since;
	}

	public void setSince(DateTime since) {
		this.since = since;
	}

	public DateTime getUntil() {
		return until;
	}

	public void setUntil(DateTime until) {
		this.until = until;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public QueryResultFormat getFormat() {
		return format;
	}

	public void setFormat(QueryResultFormat format) {
		this.format = format;
	}

	public Boolean getAscending() {
		return ascending;
	}

	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}
}
