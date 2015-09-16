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
package com.rusticisoftware.tincan.v095;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.rusticisoftware.tincan.Agent;
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
    private TCAPIVersion version = TCAPIVersion.V095;
    
    private URI verbID;
    private QueryableStatementTarget object;
    private UUID registration;
    private Boolean context;
    private Agent actor;
    private DateTime since;
    private DateTime until;
    private Integer limit;
    private Boolean authoritative;
    private Boolean sparse;
    private Agent instructor;
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

        if (this.getVerbID() != null) {
            params.put("verb", this.getVerbID().toString());
        }
        if (this.getObject() != null) {
            params.put("object", this.getObject().toJSON(getVersion()));
        }
        if (this.getRegistration() != null) {
            params.put("registration", this.getRegistration().toString());
        }
        if (this.getContext() != null) {
            params.put("context", this.getContext().toString());
        }
        if (this.getActor() != null) {
            params.put("actor", this.getActor().toJSON(getVersion()));
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
        if (this.getAuthoritative() != null) {
            params.put("authoritative", this.getAuthoritative().toString());
        }
        if (this.getSparse() != null) {
            params.put("sparse", this.getSparse().toString());
        }
        if (this.getInstructor() != null) {
            params.put("instructor", this.getInstructor().toJSON(getVersion()));
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

	public URI getVerbID() {
		return verbID;
	}

	public void setVerbID(URI verbID) {
		this.verbID = verbID;
	}

	public QueryableStatementTarget getObject() {
		return object;
	}

	public void setObject(QueryableStatementTarget object) {
		this.object = object;
	}

	public UUID getRegistration() {
		return registration;
	}

	public void setRegistration(UUID registration) {
		this.registration = registration;
	}

	public Boolean getContext() {
		return context;
	}

	public void setContext(Boolean context) {
		this.context = context;
	}

	public Agent getActor() {
		return actor;
	}

	public void setActor(Agent actor) {
		this.actor = actor;
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

	public Boolean getAuthoritative() {
		return authoritative;
	}

	public void setAuthoritative(Boolean authoritative) {
		this.authoritative = authoritative;
	}

	public Boolean getSparse() {
		return sparse;
	}

	public void setSparse(Boolean sparse) {
		this.sparse = sparse;
	}

	public Agent getInstructor() {
		return instructor;
	}

	public void setInstructor(Agent instructor) {
		this.instructor = instructor;
	}

	public Boolean getAscending() {
		return ascending;
	}

	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}
}
