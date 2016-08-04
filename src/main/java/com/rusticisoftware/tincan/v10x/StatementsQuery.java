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

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
public class StatementsQuery implements StatementsQueryInterface {
    @Getter private TCAPIVersion version = TCAPIVersion.V100;

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
    private Boolean attachments;
    private Boolean ascending;

    public void setVerbID(String verbID) throws URISyntaxException {
        this.verbID = new URI(verbID);
    }

    public void setVerbID(Verb verb) throws URISyntaxException {
        this.setVerbID(verb.getId().toString());
    }

    public HashMap<String,String> toParameterMap() throws IOException {
        HashMap<String,String> params = new HashMap<String,String>();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();

        if (this.getAgent() != null) {
            params.put("agent", this.getAgent().toJSON(version));
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

        if (this.getAttachments() != null) {
            params.put("attachments", this.getAttachments().toString());
        }

        return params;
    }
}
