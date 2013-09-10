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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import lombok.extern.java.Log;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.v10x.StatementsQuery;

@Log
public class RemoteLRSTest {
    private static final Properties config = new Properties();

    @BeforeClass
    public static void setupOnce() throws IOException {
        InputStream is = RemoteLRSTest.class.getResourceAsStream("/lrs.properties");
        config.load(is);
        is.close();
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

        obj.setVersion(TCAPIVersion.V095);
        Assert.assertEquals(TCAPIVersion.V095, obj.getVersion());
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
    public void testSaveStatement() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.stamp(); // triggers a PUT
        st.setActor(mockAgent());
        st.setVerb(mockVerbDisplay());
        st.setObject(mockActivity("testSaveStatement"));

        obj.saveStatement(st);
    }

    /*
     * Tests calling saveStatement without an ID which triggers a POST request
     */
    @Test
    public void testSaveStatementNoID() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.setActor(mockAgent());
        st.setVerb(mockVerbDisplay());
        st.setObject(mockActivity("testSaveStatementNoID"));

        obj.saveStatement(st);
    }

    @Test
    public void testSaveStatements() throws Exception {
        RemoteLRS obj = getLRS();

        List<Statement> sts = new ArrayList<Statement>();

        Statement st0 = new Statement();
        st0.stamp();
        st0.setActor(mockAgent());
        st0.setVerb(mockVerbDisplay());
        st0.setObject(mockActivity("testSaveStatements1"));

        sts.add(st0);

        Statement st1 = new Statement();
        st1.stamp();
        st1.setActor(mockAgent());
        st1.setVerb(mockVerbDisplay());
        st1.setObject(mockActivity("testSaveStatements2"));

        sts.add(st1);

        List<String> ids = obj.saveStatements(sts);
        for(String id: ids) {
            log.info("id: " + id);
        }
    }

    @Test
    public void testSaveStatementsNoIDs() throws Exception {
        RemoteLRS obj = getLRS();

        List<Statement> sts = new ArrayList<Statement>();

        Statement st0 = new Statement();
        st0.setActor(mockAgent());
        st0.setVerb(mockVerbDisplay());
        st0.setObject(mockActivity("testSaveStatementsNoIDs1"));

        sts.add(st0);

        Statement st1 = new Statement();
        st1.setActor(mockAgent());
        st1.setVerb(mockVerbDisplay());
        st1.setObject(mockActivity("testSaveStatementsNoIDs2"));

        sts.add(st1);

        List<String> ids = obj.saveStatements(sts);
        for(String id: ids) {
            log.info("id: " + id);
        }
    }

    @Test
    public void testSaveStatementsMixed() throws Exception {
        RemoteLRS obj = getLRS();

        List<Statement> sts = new ArrayList<Statement>();

        Statement st0 = new Statement();
        st0.stamp();
        st0.setActor(mockAgent());
        st0.setVerb(mockVerbDisplay());
        st0.setObject(mockActivity("testSaveStatements1"));

        sts.add(st0);

        Statement st1 = new Statement();
        st1.setActor(mockAgent());
        st1.setVerb(mockVerbDisplay());
        st1.setObject(mockActivity("testSaveStatements2"));

        sts.add(st1);

        List<String> ids = obj.saveStatements(sts);
        for(String id: ids) {
            log.info("id: " + id);
        }
    }

    @Test
    public void testRetrieveStatement() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.stamp();
        st.setActor(mockAgent());
        st.setVerb(mockVerb());
        st.setObject(mockActivity("testRetrieveStatement"));
        obj.saveStatement(st);

        Statement result = obj.retrieveStatement(st.getId().toString());
        //log.info("statement: " + result.toJSON(true));
    }
    
    @Test
    public void testRetrieveVoidedStatement() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.stamp();
        st.setActor(mockAgent());
        st.setVerb(mockVerb());
        st.setObject(mockActivity("testRetrieveVoidedStatement"));
        obj.saveStatement(st);
        
        Statement voiding = new Statement();
        voiding.stamp();
        voiding.setActor(mockAgent());
        voiding.setVerb(new Verb("http://adlnet.gov/expapi/verbs/voided"));
        voiding.setObject(new StatementRef(st.getId()));
        obj.saveStatement(voiding);

        //Should be null for 1.0
        Statement result = obj.retrieveStatement(st.getId().toString());
        log.info("statement: " + ((result == null) ? "null" : result.toJSON(true)));
        
        result = obj.retrieveVoidedStatement(st.getId().toString());
        log.info("voided statement: " + result.toJSON(true));
    }

    @Test
    public void testQueryStatementsNull() throws Exception {
        RemoteLRS obj = getLRS();
        StatementsResult result = obj.queryStatements(null);
        log.info(result.toJSON(true));
    }

    @Test
    public void testQueryStatements_V095() throws Exception {
        RemoteLRS obj = getLRS(TCAPIVersion.V095);

        com.rusticisoftware.tincan.v095.StatementsQuery query;
        query = new com.rusticisoftware.tincan.v095.StatementsQuery();
        
        query.setSince(new DateTime("2013-03-13T14:17:42.610Z"));
        //query.setLimit(3);
        query.setActor(mockAgent());
        query.setObject(mockActivity("testSaveStatement"));

        StatementsResult result = obj.queryStatements(query);
        log.info(result.toJSON(true));
    }
    
    @Test
    public void testQueryStatements() throws Exception {
        RemoteLRS obj = getLRS();

        StatementsQuery query = new StatementsQuery();
        query.setSince(new DateTime("2013-03-13T14:17:42.610Z"));
        //query.setLimit(3);
        query.setAgent(mockAgent());
        query.setActivityID(mockActivity("testSaveStatement").getId());

        StatementsResult result = obj.queryStatements(query);
        log.info(result.toJSON(true));
    }

    @Test
    public void testMoreStatements() throws Exception {
        RemoteLRS obj = getLRS();

        StatementsQuery query = new StatementsQuery();
        query.setLimit(3);
        StatementsResult queryResult = obj.queryStatements(query);
        log.info("statement count: " + queryResult.getStatements().size());
        log.info("result - more: " + queryResult.getMoreURL());

        StatementsResult moreResult = obj.moreStatements(queryResult.getMoreURL());
        log.info("statement count: " + moreResult.getStatements().size());
        log.info("result - more: " + moreResult.getMoreURL());
    }

    @Test
    public void testSaveState() throws Exception {
        RemoteLRS obj = getLRS();

        String key = "testRetrieveState";
        String value = "Test";
        Agent agent = mockAgent();
        URI activityId = mockActivity(key).getId();

        State state = new State(key, value, activityId, agent, null);
        obj.saveState(state, mockActivity(key).getId().toString(), mockAgent(), null);
    }

    @Test
    public void testRetrieveState() throws Exception {
        RemoteLRS obj = getLRS();

        String key = "testRetrieveState";
        String value = "Test";
        Agent agent = mockAgent();
        URI activityId = mockActivity(key).getId();

        State state = new State(key, value, activityId, agent, null);
        obj.saveState(state, mockActivity(key).getId().toString(), mockAgent(), null);

        State retrievedState = obj.retrieveState(key, mockActivity(key).getId().toString(), mockAgent(), null);
        Assert.assertEquals(key, retrievedState.getId());
        Assert.assertEquals(value, new String(retrievedState.getContents()));
    }

    private RemoteLRS getLRS() throws Exception {
        return getLRS(TCAPIVersion.V100);
    }
    
    private RemoteLRS getLRS(TCAPIVersion version) throws Exception {
        RemoteLRS obj = new RemoteLRS();
        obj.setEndpoint(config.getProperty("endpoint"));
        obj.setVersion(version);
        obj.setUsername(config.getProperty("username"));
        obj.setPassword(config.getProperty("password"));

        return obj;
    }

    private Statement mockStatement() {
        Statement obj = new Statement();

        return obj;
    }

    private Agent mockAgent() {
        Agent obj = new Agent();
        obj.setMbox("mailto:tincanjava-test-tincan@tincanapi.com");

        return obj;
    }

    private Verb mockVerb() throws URISyntaxException {
        return new Verb("http://adlnet.gov/expapi/verbs/attempted");
    }

    private Verb mockVerbDisplay() throws URISyntaxException {
        Verb obj = mockVerb();
        LanguageMap display = new LanguageMap();
        display.put("und", obj.getId().toString());
        display.put("en-US", "attempted");

        obj.setDisplay(display);

        return obj;
    }

    private Activity mockActivity(String suffix) throws URISyntaxException {
        return new Activity("http://tincanapi.com/TinCanJava/Test/RemoteLRSTest_mockActivity/" + suffix);
    }
}
