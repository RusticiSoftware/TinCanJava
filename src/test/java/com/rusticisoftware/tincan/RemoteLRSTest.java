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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.MessageDigest;
import java.util.*;

import com.rusticisoftware.tincan.documents.ActivityProfileDocument;
import com.rusticisoftware.tincan.documents.AgentProfileDocument;
import com.rusticisoftware.tincan.documents.StateDocument;
import com.rusticisoftware.tincan.lrsresponses.*;
import com.rusticisoftware.tincan.json.*;

import lombok.extern.java.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.codec.binary.Hex;
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
    private static Attachment attachment1;
    private static Attachment attachment2;
    private static Attachment attachment3;

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

        attachment1 = new Attachment();
        attachment1.setContent("hello world".getBytes("UTF-8"));
        attachment1.setContentType("application/octet-stream");
        attachment1.setDescription(new LanguageMap());
        attachment1.getDescription().put("en-US", "Test Description");
        attachment1.setDisplay(new LanguageMap());
        attachment1.getDisplay().put("en-US", "Test Display");
        attachment1.setUsageType(new URI("http://id.tincanapi.com/attachment/supporting_media"));

        attachment2 = new Attachment();
        attachment2.setContent("hello world 2".getBytes("UTF-8"));
        attachment2.setContentType("text/plain");
        attachment2.setDescription(new LanguageMap());
        attachment2.getDescription().put("en-US", "Test Description 2");
        attachment2.setDisplay(new LanguageMap());
        attachment2.getDisplay().put("en-US", "Test Display 2");
        attachment2.setUsageType(new URI("http://id.tincanapi.com/attachment/supporting_media"));


        attachment3 = new Attachment();
        attachment3.setContent(getResourceAsByteArray("/files/image.jpg"));
        attachment3.setContentType("image/jpeg");
        attachment3.setDescription(new LanguageMap());
        attachment3.getDescription().put("en-US", "Test Description 3");
        attachment3.setDisplay(new LanguageMap());
        attachment3.getDisplay().put("en-US", "Test Display 3");
        attachment3.setUsageType(new URI("http://id.tincanapi.com/attachment/supporting_media"));
    }

    //
    // see http://stackoverflow.com/a/1264737/1464957
    //
    private static byte[] getResourceAsByteArray(String resourcePath) throws IOException {
        InputStream resourceIs = RemoteLRSTest.class.getResourceAsStream(resourcePath);
        ByteArrayOutputStream resourceData = new ByteArrayOutputStream();

        int nRead;
        byte[] buffer = new byte[16384];

        while ((nRead = resourceIs.read(buffer, 0, buffer.length)) != -1) {
            resourceData.write(buffer, 0, nRead);
        }

        resourceData.flush();

        return resourceData.toByteArray();
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
    public void testSaveStatementWithAttachment() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment1);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
        Assert.assertNotNull(lrsRes.getContent().getId());
        Assert.assertNotNull(lrsRes.getResponse().getContent());
    }

    @Test
    public void testSaveStatementWithAttachments() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment1);
        statement.addAttachment(attachment2);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
        Assert.assertNotNull(lrsRes.getContent().getId());
        Assert.assertNotNull(lrsRes.getResponse().getContent());
    }

    @Test
    public void testSaveStatementsWithAttachment() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment1);

        List<Statement> statementList = new ArrayList<Statement>();
        statementList.add(statement);

        statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statementList.add(statement);

        StatementsResultLRSResponse lrsResultResp = lrs.saveStatements(statementList);
        Assert.assertTrue(lrsResultResp.getSuccess());
        Assert.assertEquals(statement, lrsResultResp.getContent().getStatements().get(1));
        Assert.assertNotNull(lrsResultResp.getContent().getStatements().get(0).getId());
        Assert.assertNotNull(lrsResultResp.getContent().getStatements().get(1).getId());
        Assert.assertNotNull(lrsResultResp.getResponse().getContent());
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
        StatementLRSResponse retRes = lrs.retrieveStatement(saveRes.getContent().getId().toString(), false);
        Assert.assertTrue(retRes.getSuccess());
    }

    @Test
    public void testRetrieveStatementWithAttachment() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment1);

        StatementLRSResponse saveRes = lrs.saveStatement(statement);
        Assert.assertTrue(saveRes.getSuccess());

        StatementLRSResponse retRes = lrs.retrieveStatement(saveRes.getContent().getId().toString(), true);
        Assert.assertTrue(retRes.getSuccess());

        String hash1, hash2;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(attachment1.getContent());
        byte[] hash = digest.digest();
        hash1 = new String(Hex.encodeHex(hash));

        digest.update(retRes.getContent().getAttachments().get(0).getContent());
        hash = digest.digest();
        hash2 = new String(Hex.encodeHex(hash));

        Assert.assertEquals(hash1, hash2);
    }

    @Test
    public void testRetrieveStatementWithBinaryAttachment() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment3);

        StatementLRSResponse saveRes = lrs.saveStatement(statement);
        Assert.assertTrue(saveRes.getSuccess());

        StatementLRSResponse retRes = lrs.retrieveStatement(saveRes.getContent().getId().toString(), true);
        Assert.assertTrue(retRes.getSuccess());

        String hash1, hash2;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(attachment3.getContent());
        byte[] hash = digest.digest();
        hash1 = new String(Hex.encodeHex(hash));

        digest.update(retRes.getContent().getAttachments().get(0).getContent());
        hash = digest.digest();
        hash2 = new String(Hex.encodeHex(hash));

        Assert.assertEquals(hash1, hash2);
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
    public void testQueryStatementsWithAttachments() throws Exception {
        Statement statement = new Statement();
        statement.setActor(agent);
        statement.setVerb(verb);
        statement.setObject(activity);
        statement.addAttachment(attachment1);

        StatementLRSResponse lrsRes = lrs.saveStatement(statement);
        Assert.assertTrue(lrsRes.getSuccess());
        Assert.assertEquals(statement, lrsRes.getContent());
        Assert.assertNotNull(lrsRes.getContent().getId());
        Assert.assertNotNull(lrsRes.getResponse().getContent());

        StatementsQuery query = new StatementsQuery();
        query.setFormat(QueryResultFormat.EXACT);
        query.setLimit(10);
        query.setAttachments(true);

        StatementsResultLRSResponse lrsStmntRes = lrs.queryStatements(query);
        Assert.assertTrue(lrsStmntRes.getSuccess());

        String hash1, hash2;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(attachment1.getContent());
        byte[] hash = digest.digest();
        hash1 = new String(Hex.encodeHex(hash));

        digest.update(lrsStmntRes.getContent().getStatements().get(0).getAttachments().get(0).getContent());
        hash = digest.digest();
        hash2 = new String(Hex.encodeHex(hash));

        Assert.assertEquals(hash1, hash2);
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
        ActivityLRSResponse lrsResponse = lrs.retrieveActivity(activity);
        Assert.assertTrue(lrsResponse.getSuccess());

        Activity returnedActivity = lrsResponse.getContent();
        Assert.assertTrue(activity.getId().toString().equals(returnedActivity.getId().toString()));
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
        PersonLRSResponse lrsResponse = lrs.retrievePerson(agent);
        Assert.assertTrue(lrsResponse.getSuccess());

        Person person = lrsResponse.getContent();
        Assert.assertTrue(agent.getName().equals(person.getName().get(0)));
        Assert.assertTrue(agent.getMbox().equals(person.getMbox().get(0)));
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
