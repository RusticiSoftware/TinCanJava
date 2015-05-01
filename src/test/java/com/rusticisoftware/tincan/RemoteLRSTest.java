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


import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import com.rusticisoftware.tincan.documents.ActivityProfileDocument;
import com.rusticisoftware.tincan.documents.AgentProfileDocument;
import com.rusticisoftware.tincan.documents.StateDocument;
import com.rusticisoftware.tincan.lrsresponses.*;
import com.rusticisoftware.tincan.json.*;

import lombok.extern.java.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rusticisoftware.tincan.v10x.StatementsQuery;

@Log
public class RemoteLRSTest {
    private static RemoteLRS lrs;
    private static Agent agent;
    private static Verb verb;
    private static Activity activity;
    private static Activity parent;
    private static Context context;
    private static Result result;
    private static Score score;
    private static StatementRef statementRef;
    private static SubStatement subStatement;

    private static Properties config = new Properties();

    @BeforeClass
    public static void Init() throws Exception {
        lrs = new RemoteLRS(TCAPIVersion.V100);

        InputStream is = RemoteLRSTest.class.getResourceAsStream("/lrs.properties");
        config.load(is);
        is.close();

        lrs.setEndpoint(config.getProperty("endpoint"));
        lrs.setUsername(config.getProperty("username"));
        lrs.setPassword(config.getProperty("password"));

        agent = new Agent();
        agent.setMbox("mailto:tincanjava@tincanapi.com");
        agent.setName("Test Agent");

        verb = new Verb("http://adlnet.gov/expapi/verbs/experienced");
        verb.setDisplay(new LanguageMap());
        verb.getDisplay().put("en-US", "experienced");

        activity = new Activity();
        activity.setId(new URI("http://tincanapi.com/TinCanJava/Test/Unit/0"));
        activity.setDefinition(new ActivityDefinition());
        activity.getDefinition().setType(new URI("http://id.tincanapi.com/activitytype/unit-test"));
        activity.getDefinition().setName(new LanguageMap());
        activity.getDefinition().getName().put("en-US", "TinCanJava Tests: Unit 0");
        activity.getDefinition().setDescription(new LanguageMap());
        activity.getDefinition().getDescription().put("en-US", "Unit test 0 in the test suite for the Tin Can Java library.");

        parent = new Activity();
        parent.setId(new URI("http://tincanapi.com/TinCanJava/Test"));
        parent.setDefinition(new ActivityDefinition());
        parent.getDefinition().setType(new URI("http://id.tincanapi.com/activitytype/unit-test-suite"));
        //parent.getDefinition().setMoreInfo(new URI("http://rusticisoftware.github.io/TinCanJava/"));
        parent.getDefinition().setName(new LanguageMap());
        parent.getDefinition().getName().put("en-US", "TinCanJavava Tests");
        parent.getDefinition().setDescription(new LanguageMap());
        parent.getDefinition().getDescription().put("en-US", "Unit test suite for the Tin Can Java library.");

        statementRef = new StatementRef(UUID.randomUUID());

        context = new Context();
        context.setRegistration(UUID.randomUUID());
        context.setStatement(statementRef);
        context.setContextActivities(new ContextActivities());
        context.getContextActivities().setParent(new ArrayList<Activity>());
        context.getContextActivities().getParent().add(parent);

        score = new Score();
        score.setRaw(97.0);
        score.setScaled(0.97);
        score.setMax(100.0);
        score.setMin(0.0);

        result = new Result();
        result.setScore(score);
        result.setSuccess(true);
        result.setCompletion(true);
        result.setDuration(new Period(1, 2, 16, 43));

        subStatement = new SubStatement();
        subStatement.setActor(agent);
        subStatement.setVerb(verb);
        subStatement.setObject(parent);
    }

    @Test
    public void testEndpoint() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        Assert.assertNull(obj.getEndpoint());

        String strURL = "http://tincanapi.com/test/TinCanJava";
        obj.setEndpoint(strURL);
        Assert.assertEquals(strURL + "/", obj.getEndpoint().toString());

    }

    @Test(expected = MalformedURLException.class)
    public void testEndPointBadURL() throws MalformedURLException {
        RemoteLRS obj = new RemoteLRS();
        obj.setEndpoint("test");
    }

    @Test
    public void testVersion() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        Assert.assertNull(obj.getVersion());

        obj.setVersion(TCAPIVersion.V100);
        Assert.assertEquals(TCAPIVersion.V100, lrs.getVersion());
    }

    @Test
    public void testAuth() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        Assert.assertNull(obj.getAuth());

        obj.setAuth("test");
        Assert.assertEquals("test", obj.getAuth());
    }

    @Test
    public void testUsername() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        obj.setPassword("pass");

        Assert.assertNull(obj.getUsername());
        Assert.assertNull(obj.getAuth());

        obj.setUsername("test");
        Assert.assertEquals("test", obj.getUsername());
        Assert.assertEquals(obj.getAuth(), "Basic dGVzdDpwYXNz");
    }

    @Test
    public void testPassword() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        obj.setUsername("user");

        Assert.assertNull(obj.getPassword());
        Assert.assertNull(obj.getAuth());

        obj.setPassword("test");
        Assert.assertEquals("test", obj.getPassword());
        Assert.assertEquals("Basic dXNlcjp0ZXN0", obj.getAuth());
    }

    @Test
    public void testExtended() throws Exception {
    }

    @Test
    public void testCalculateBasicAuth() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        obj.setUsername("user");
        obj.setPassword("pass");
        Assert.assertEquals("Basic dXNlcjpwYXNz", obj.calculateBasicAuth());
    }

    @Test
    public void testAbout() throws Exception {
        AboutLRSResponse lrsRes = lrs.about();
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testAboutFailure() throws Exception {
        RemoteLRS obj = new RemoteLRS(TCAPIVersion.V100);
        obj.setEndpoint(new URI("http://cloud.scorm.com/tc/3TQLAI9/sandbox/").toString());

        AboutLRSResponse lrsRes = obj.about();
        Assert.assertFalse(lrsRes.getSuccess());
    }

    @Test
    public void testSaveStatement() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
        Assert.assertNotNull(lrsRes.getContent().getId());
    }

    @Test
    public void testSaveStatementWithID() throws Exception {
        Statement statement = new Statement();
        statement.stamp();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
    }

    @Test
    public void testSaveStatementWithContext() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.setContext(context);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
    }

    @Test
    public void testSaveStatementWithResult() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.setContext(context);
        statement.setResult(result);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
    }

    @Test
    public void testSaveStatementStatementRef() throws Exception {
        Statement statement = new Statement();
        statement.stamp();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(statementRef);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
    }

    @Test
    public void testSaveStatementSubStatement() throws Exception {
        Statement statement = new Statement();
        statement.stamp();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(subStatement);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
    }

    @Test
    public void testSaveStatements() throws Exception {
        Statement statement1 = new Statement();
        statement1.setActor(agent);
        statement1.setVerb(verb);
        statement1.setObject(parent);

        Statement statement2 = new Statement();
        statement2.setActor(agent);
        statement2.setVerb(verb);
        statement2.setObject(activity);
        statement2.setContext(context);

        List<Statement> statements = new ArrayList<Statement>();
        statements.add(statement1);
        statements.add(statement2);

        StatementsResultLRSResponse lrsRes = lrs.saveStatements(statements);
        Assert.assertTrue(lrsRes.getSuccess());

        Statement s1 = lrsRes.getContent().getStatements().get(0);
        Statement s2 = lrsRes.getContent().getStatements().get(1);

        Assert.assertNotNull(s1.getId());
        Assert.assertNotNull(s2.getId());

        Assert.assertEquals(s1.getActor(), agent);
        Assert.assertEquals(s1.getVerb(), verb);
        Assert.assertEquals(s1.getObject(), parent);

        Assert.assertEquals(s2.getActor(), agent);
        Assert.assertEquals(s2.getVerb(), verb);
        Assert.assertEquals(s2.getObject(), activity);
        Assert.assertEquals(s2.getContext(), context);
    }

    @Test
    public void testRetrieveStatement() throws Exception {
        Statement statement = new Statement();
        statement.stamp();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.setContext(context);
        statement.setResult(result);

        StatementLRSResponse saveRes = lrs.saveStatement(statement);
        Assert.assertTrue(saveRes.getSuccess());
        StatementLRSResponse retRes = lrs.retrieveStatement(saveRes.getContent().getId().toString());
        Assert.assertTrue(retRes.getSuccess());
    }

    @Test
    public void testQueryStatements() throws Exception {
        StatementsQuery query = new StatementsQuery();
        query.setAgent(agent);
        query.setVerbID(verb.getId().toString());
        query.setActivityID(parent.getId());
        query.setRelatedActivities(true);
        query.setRelatedAgents(true);
        query.setFormat(QueryResultFormat.IDS);
        query.setLimit(10);

        StatementsResultLRSResponse lrsRes = lrs.queryStatements(query);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testMoreStatements() throws Exception {
        StatementsQuery query = new StatementsQuery();
        query.setFormat(QueryResultFormat.IDS);
        query.setLimit(2);

        StatementsResultLRSResponse queryRes = lrs.queryStatements(query);
        Assert.assertTrue(queryRes.getSuccess());
        Assert.assertNotNull(queryRes.getContent().getMoreURL());
        StatementsResultLRSResponse moreRes = lrs.moreStatements(queryRes.getContent().getMoreURL());
        Assert.assertTrue(moreRes.getSuccess());
    }

    @Test
    public void testRetrieveStateIds() throws Exception {
        ProfileKeysLRSResponse lrsRes = lrs.retrieveStateIds(activity, agent, null);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testRetrieveState() throws Exception {
        LRSResponse clear = lrs.clearState(activity, agent, null);
        Assert.assertTrue(clear.getSuccess());

        StateDocument doc = new StateDocument();
        doc.setActivity(activity);
        doc.setAgent(agent);
        doc.setId("test");
        doc.setContent("Test value".getBytes("UTF-8"));

        LRSResponse save = lrs.saveState(doc);
        Assert.assertTrue(save.getSuccess());

        StateLRSResponse lrsRes = lrs.retrieveState("test", activity, agent, null);
        Assert.assertEquals("\"c140f82cb70e3884ad729b5055b7eaa81c795f1f\"", lrsRes.getContent().getEtag());
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testSaveState() throws Exception {
        StateDocument doc = new StateDocument();
        doc.setActivity(activity);
        doc.setAgent(agent);
        doc.setId("test");
        doc.setContent("Test value".getBytes("UTF-8"));

        LRSResponse lrsRes = lrs.saveState(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testOverwriteState() throws Exception {
        LRSResponse clear = lrs.clearState(activity, agent, null);
        Assert.assertTrue(clear.getSuccess());

        StateDocument doc = new StateDocument();
        doc.setActivity(activity);
        doc.setAgent(agent);
        doc.setId("test");
        doc.setContent("Test value".getBytes("UTF-8"));

        LRSResponse save = lrs.saveState(doc);
        Assert.assertTrue(save.getSuccess());

        StateLRSResponse retrieve = lrs.retrieveState("test", activity, agent, null);
        Assert.assertTrue(retrieve.getSuccess());

        doc.setEtag(retrieve.getContent().getEtag());
        doc.setId("testing");
        doc.setActivity(parent);
        LRSResponse lrsResp = lrs.saveState(doc);
        Assert.assertTrue(lrsResp.getSuccess());
    }

    @Test
    public void testUpdateState() throws Exception {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode changeSet = mapper.createObjectNode();  // What changes are to be made
        ObjectNode correctSet = mapper.createObjectNode(); // What the correct content should be after change
        ObjectNode currentSet = mapper.createObjectNode(); // What the actual content is after change

        // Load initial change set
        String data = "{ \"x\" : \"foo\", \"y\" : \"bar\" }";
        Map<String, String> changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }
        Map<String, String> correctSetMap = changeSetMap; // In the beginning, these are equal
        for (String k : correctSetMap.keySet()) {
            String v = correctSetMap.get(k);
            correctSet.put(k, v);
        }

        StateDocument doc = new StateDocument();
        doc.setActivity(activity);
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse clear = lrs.deleteState(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContentType("application/json");
        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        LRSResponse save = lrs.saveState(doc);
        Assert.assertTrue(save.getSuccess());
        StateLRSResponse retrieveBeforeUpdate = lrs.retrieveState("test", activity, agent, null);
        Assert.assertTrue(retrieveBeforeUpdate.getSuccess());
        StateDocument beforeDoc = retrieveBeforeUpdate.getContent();
        Map<String, String> c = mapper.readValue(new String(beforeDoc.getContent(), "UTF-8"), Map.class);
        for (String k : c.keySet()) {
            String v = c.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));

        doc.setContentType("application/json");
        data = "{ \"x\" : \"bash\", \"z\" : \"faz\" }";
        changeSet.removeAll();
        changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }

        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        // Update the correct set with the changes
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            correctSet.put(k, v);
        }

        currentSet.removeAll();

        LRSResponse update = lrs.updateState(doc);
        Assert.assertTrue(update.getSuccess());
        StateLRSResponse retrieveAfterUpdate = lrs.retrieveState("test", activity, agent, null);
        Assert.assertTrue(retrieveAfterUpdate.getSuccess());
        StateDocument afterDoc = retrieveAfterUpdate.getContent();
        Map<String, String> ac = mapper.readValue(new String(afterDoc.getContent(), "UTF-8"), Map.class);
        for (String k : ac.keySet()) {
            String v = ac.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));
    }

    @Test
    public void testDeleteState() throws Exception {
        StateDocument doc = new StateDocument();
        doc.setActivity(activity);
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse lrsRes = lrs.deleteState(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testClearState() throws Exception {
        LRSResponse lrsRes = lrs.clearState(activity, agent, null);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testRetrieveActivity() throws Exception {
        ActivityProfileLRSResponse lrsResponse = lrs.retrieveActivity(activity);
    	Assert.assertTrue(lrsResponse.getSuccess());

    	ActivityProfileDocument returnedDoc = lrsResponse.getContent();
    	Assert.assertNull(returnedDoc.getId());    // Retrieving the full Activity does not return a profile ID
    	Activity returnedActivity = returnedDoc.getActivity();
    	Assert.assertTrue(activity.getId().toString().equals(returnedActivity.getId().toString()));
    	
    	// Test for non-existent activity
    	Activity noActivity = new Activity();
    	noActivity.setId("https://brindlewaye.com/xAPITerms/Activity/NeverGonnaHappen/");
    	
        ActivityProfileLRSResponse lrsResponse2 = lrs.retrieveActivity(noActivity);
        // Report success even though response status was 404
    	Assert.assertTrue(lrsResponse2.getSuccess());
    	Assert.assertTrue(lrsResponse2.getResponse().getStatus() == 404);

    	ActivityProfileDocument returnedDoc2 = lrsResponse2.getContent();
    	Assert.assertNull(returnedDoc2);
    }
    
    @Test
    public void testRetrieveActivityProfileIds() throws Exception {
        ProfileKeysLRSResponse lrsRes = lrs.retrieveActivityProfileIds(activity);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testRetrieveActivityProfile() throws Exception {
        ActivityProfileDocument doc = new ActivityProfileDocument();
        doc.setActivity(activity);
        doc.setId("test");

        LRSResponse clear = lrs.deleteActivityProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value2".getBytes("UTF-8"));

        LRSResponse save = lrs.saveActivityProfile(doc);
        Assert.assertTrue(save.getSuccess());

        ActivityProfileLRSResponse lrsRes = lrs.retrieveActivityProfile("test", activity);
        Assert.assertEquals("\"6e6e6c11d7e0bffe0369873a2a5fd751ab2ea64f\"", lrsRes.getContent().getEtag());
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testSaveActivityProfile() throws Exception {
        ActivityProfileDocument doc = new ActivityProfileDocument();
        doc.setActivity(activity);
        doc.setId("test");

        LRSResponse clear = lrs.deleteActivityProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value2".getBytes("UTF-8"));

        LRSResponse lrsRes = lrs.saveActivityProfile(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testUpdateActivityProfile() throws Exception {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode changeSet = mapper.createObjectNode();  // What changes are to be made
        ObjectNode correctSet = mapper.createObjectNode(); // What the correct content should be after change
        ObjectNode currentSet = mapper.createObjectNode(); // What the actual content is after change

        // Load initial change set
        String data = "{ \"x\" : \"foo\", \"y\" : \"bar\" }";
        Map<String, String> changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }
        Map<String, String> correctSetMap = changeSetMap; // In the beginning, these are equal
        for (String k : correctSetMap.keySet()) {
            String v = correctSetMap.get(k);
            correctSet.put(k, v);
        }

        ActivityProfileDocument doc = new ActivityProfileDocument();
        doc.setActivity(activity);
        doc.setId("test");

        LRSResponse clear = lrs.deleteActivityProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContentType("application/json");
        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        LRSResponse save = lrs.saveActivityProfile(doc);
        Assert.assertTrue(save.getSuccess());
        ActivityProfileLRSResponse retrieveBeforeUpdate = lrs.retrieveActivityProfile("test", activity);
        Assert.assertTrue(retrieveBeforeUpdate.getSuccess());
        ActivityProfileDocument beforeDoc = retrieveBeforeUpdate.getContent();
        Map<String, String> c = mapper.readValue(new String(beforeDoc.getContent(), "UTF-8"), Map.class);
        for (String k : c.keySet()) {
            String v = c.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));

        doc.setContentType("application/json");
        data = "{ \"x\" : \"bash\", \"z\" : \"faz\" }";
        changeSet.removeAll();
        changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }

        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        // Update the correct set with the changes
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            correctSet.put(k, v);
        }

        currentSet.removeAll();

        LRSResponse update = lrs.updateActivityProfile(doc);
        Assert.assertTrue(update.getSuccess());
        ActivityProfileLRSResponse retrieveAfterUpdate = lrs.retrieveActivityProfile("test", activity);
        Assert.assertTrue(retrieveAfterUpdate.getSuccess());
        ActivityProfileDocument afterDoc = retrieveAfterUpdate.getContent();
           Map<String, String> ac = mapper.readValue(new String(afterDoc.getContent(), "UTF-8"), Map.class);
        for (String k : ac.keySet()) {
            String v = ac.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));
    }

    @Test
    public void testOverwriteActivityProfile() throws Exception {
        ActivityProfileDocument doc = new ActivityProfileDocument();
        doc.setActivity(activity);
        doc.setId("test");

        LRSResponse clear = lrs.deleteActivityProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value2".getBytes("UTF-8"));

        LRSResponse save = lrs.saveActivityProfile(doc);
        Assert.assertTrue(save.getSuccess());

        ActivityProfileLRSResponse retrieve = lrs.retrieveActivityProfile("test", activity);
        Assert.assertTrue(retrieve.getSuccess());

        doc.setEtag(retrieve.getContent().getEtag());
        doc.setId("test2");
        doc.setContent("Test value3".getBytes("UTF-8"));

        LRSResponse lrsResp = lrs.saveActivityProfile(doc);
        Assert.assertTrue(lrsResp.getSuccess());
    }

    @Test
    public void testDeleteActivityProfile() throws Exception {
        ActivityProfileDocument doc = new ActivityProfileDocument();
        doc.setActivity(activity);
        doc.setId("test");

        LRSResponse lrsRes = lrs.deleteActivityProfile(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testRetrievePerson() throws Exception {
        AgentProfileLRSResponse lrsResponse = lrs.retrievePerson(agent);
    	Assert.assertTrue(lrsResponse.getSuccess());

    	AgentProfileDocument returnedDoc = lrsResponse.getContent();
    	Assert.assertNull(returnedDoc.getId());    // Retrieving the full Agent does not return a profile ID
    	Agent returnedAgent = returnedDoc.getAgent();
    	Assert.assertTrue(agent.getName().equals(returnedAgent.getName()));
    	
    	// Test for non-existent agent
    	Agent noAgent = new Agent();
    	noAgent.setMbox("mailto:noone@example.com");
    	noAgent.setName("No One");
    	
        AgentProfileLRSResponse lrsResponse2 = lrs.retrievePerson(noAgent);
    	Assert.assertTrue(lrsResponse2.getSuccess());
    	Assert.assertTrue(lrsResponse2.getResponse().getStatus() == 200);
    	
    	Agent returnedAgent2 = lrsResponse2.getContent().getAgent();
    	// The spec says that this call will return an object built from the information provided in the parameter Agent if it finds nothing matching at the endpoint
    	Assert.assertTrue(returnedAgent2.equals(noAgent));
    }
    
    @Test
    public void testRetrieveAgentProfileIds() throws Exception {
        ProfileKeysLRSResponse lrsRes = lrs.retrieveAgentProfileIds(agent);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testRetrieveAgentProfile() throws Exception {
        AgentProfileDocument doc = new AgentProfileDocument();
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse clear = lrs.deleteAgentProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value4".getBytes("UTF-8"));

        LRSResponse save = lrs.saveAgentProfile(doc);
        Assert.assertTrue(save.getSuccess());

        AgentProfileLRSResponse lrsRes = lrs.retrieveAgentProfile("test", agent);
        Assert.assertEquals("\"da16d3e0cbd55e0f13558ad0ecfd2605e2238c71\"", lrsRes.getContent().getEtag());
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testSaveAgentProfile() throws Exception {
        AgentProfileDocument doc = new AgentProfileDocument();
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse clear = lrs.deleteAgentProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value".getBytes("UTF-8"));

        LRSResponse lrsRes = lrs.saveAgentProfile(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }

    @Test
    public void testUpdateAgentProfile() throws Exception {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode changeSet = mapper.createObjectNode();  // What changes are to be made
        ObjectNode correctSet = mapper.createObjectNode(); // What the correct content should be after change
        ObjectNode currentSet = mapper.createObjectNode(); // What the actual content is after change

        // Load initial change set
        String data = "{ \"firstName\" : \"Dave\", \"lastName\" : \"Smith\", \"State\" : \"CO\" }";
        Map<String, String> changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }
        Map<String, String> correctSetMap = changeSetMap; // In the beginning, these are equal
        for (String k : correctSetMap.keySet()) {
            String v = correctSetMap.get(k);
            correctSet.put(k, v);
        }

        AgentProfileDocument doc = new AgentProfileDocument();
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse clear = lrs.deleteAgentProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContentType("application/json");
        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        LRSResponse save = lrs.saveAgentProfile(doc);
        Assert.assertTrue(save.getSuccess());
        AgentProfileLRSResponse retrieveBeforeUpdate = lrs.retrieveAgentProfile("test", agent);
        Assert.assertTrue(retrieveBeforeUpdate.getSuccess());
        AgentProfileDocument beforeDoc = retrieveBeforeUpdate.getContent();
        Map<String, String> c = mapper.readValue(new String(beforeDoc.getContent(), "UTF-8"), Map.class);
        for (String k : c.keySet()) {
            String v = c.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));

        doc.setContentType("application/json");
        data = "{ \"lastName\" : \"Jones\", \"City\" : \"Colorado Springs\" }";
        changeSet.removeAll();
        changeSetMap = mapper.readValue(data, Map.class);
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            changeSet.put(k, v);
        }

        doc.setContent(changeSet.toString().getBytes("UTF-8"));

        // Update the correct set with the changes
        for (String k : changeSetMap.keySet()) {
            String v = changeSetMap.get(k);
            correctSet.put(k, v);
        }

        currentSet.removeAll();
        LRSResponse update = lrs.updateAgentProfile(doc);
        Assert.assertTrue(update.getSuccess());
        AgentProfileLRSResponse retrieveAfterUpdate = lrs.retrieveAgentProfile("test", agent);
        Assert.assertTrue(retrieveAfterUpdate.getSuccess());
        AgentProfileDocument afterDoc = retrieveAfterUpdate.getContent();
           Map<String, String> ac = mapper.readValue(new String(afterDoc.getContent(), "UTF-8"), Map.class);
        for (String k : ac.keySet()) {
            String v = ac.get(k);
            currentSet.put(k, v);
        }
        Assert.assertTrue(currentSet.equals(correctSet));
    }

    @Test
    public void testOverwriteAgentProfile() throws Exception {
        AgentProfileDocument doc = new AgentProfileDocument();
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse clear = lrs.deleteAgentProfile(doc);
        Assert.assertTrue(clear.getSuccess());

        doc.setContent("Test value4".getBytes("UTF-8"));

        LRSResponse save = lrs.saveAgentProfile(doc);
        Assert.assertTrue(save.getSuccess());

        AgentProfileLRSResponse retrieve = lrs.retrieveAgentProfile("test", agent);
        Assert.assertTrue(retrieve.getSuccess());

        doc.setEtag(retrieve.getContent().getEtag());
        doc.setId("test2");
        doc.setContent("Test value5".getBytes("UTF-8"));

        LRSResponse lrsResp = lrs.saveAgentProfile(doc);
        Assert.assertTrue(lrsResp.getSuccess());
    }

    @Test
    public void testDeleteAgentProfile() throws Exception {
        AgentProfileDocument doc = new AgentProfileDocument();
        doc.setAgent(agent);
        doc.setId("test");

        LRSResponse lrsRes = lrs.deleteAgentProfile(doc);
        Assert.assertTrue(lrsRes.getSuccess());
    }
}
