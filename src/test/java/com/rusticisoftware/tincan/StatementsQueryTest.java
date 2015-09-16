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

import static com.rusticisoftware.tincan.TestUtils.*;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;

import com.rusticisoftware.tincan.v10x.StatementsQuery;

/**
 * StatementsQueryTest Class Description
 */
public class StatementsQueryTest {
    @Test
    public void v10SerializeDeserialize() throws Exception {
        StatementsQuery query = new StatementsQuery();
        query.setActivityID(new URI("http://example.com/activity"));
        query.setAgent(getAgent("Joe", "mbox", "mailto:joeuser@example.com"));
        query.setAscending(true);
        query.setFormat(QueryResultFormat.EXACT);
        query.setLimit(10);
        query.setRegistration(UUID.randomUUID());
        query.setRelatedActivities(true);
        query.setRelatedAgents(true);
        query.setSince(new DateTime());
        query.setUntil(new DateTime());
        query.setVerbID("http://example.com/verb");
        
        //Rudimentary, but something...
        List<String> expected = new ArrayList<String>(
                Arrays.asList(new String[]{ 
                    "agent", "verb", "activity", "registration", 
                    "related_activities", "related_agents", "since",
                    "until", "limit", "format", "ascending"
                }));
        
        Map<String, String> paramMap = query.toParameterMap();
        for (String key : expected) {
            assertTrue(paramMap.containsKey(key));
        }
    }
    
    public void v95SerializeDeserialize() throws Exception {
        com.rusticisoftware.tincan.v095.StatementsQuery query;
        query = new com.rusticisoftware.tincan.v095.StatementsQuery();
        query.setActor(getAgent("Joe", "mbox", "mailto:joeuser@example.com"));
        query.setAscending(true);
        query.setAuthoritative(true);
        query.setContext(true);
        query.setInstructor(getAgent("Instructor", "mbox", "mailto:instructor@example.com"));
        query.setLimit(10);
        query.setObject(new Activity("http://example.com/activity"));
        query.setRegistration(UUID.randomUUID());
        query.setSince(new DateTime());
        query.setSparse(true);
        query.setUntil(new DateTime());
        query.setVerbID("http://example.com/verb");
        
        List<String> expected = new ArrayList<String>(
                Arrays.asList(new String[]{ 
                    "actor", "verb", "object", "registration", "context", 
                    "since", "until", "limit", "authoritative", 
                    "sparse", "instructor", "ascending"
                }));
        
        Map<String, String> paramMap = query.toParameterMap();
        for (String key : expected) {
            assertTrue(paramMap.containsKey(key));
        }
    }
}
