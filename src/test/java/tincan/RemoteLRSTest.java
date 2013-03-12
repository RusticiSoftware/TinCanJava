package tincan;

import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

@Log
public class RemoteLRSTest {
    @Test
    public void testEndpoint() throws Exception {
        RemoteLRS obj = new RemoteLRS();
        Assert.assertNull(obj.getEndpoint());

        String strURL = "http://tincanapi.com/test/TinCanJava";
        obj.setEndpoint(strURL);
        Assert.assertEquals(strURL, obj.getEndpoint().toString());

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
    public void testPutStatement() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.stamp(); // triggers a PUT
        st.setActor(mockAgent());
        st.setVerb(mockVerbDisplay());
        st.setObject(mockActivity("testPutStatement"));

        obj.putStatement(st);
    }

    /*
     * Tests calling putStatement without an ID which triggers a POST request
     */
    @Test
    public void testPutStatementNoID() throws Exception {
        RemoteLRS obj = getLRS();

        Statement st = new Statement();
        st.setActor(mockAgent());
        st.setVerb(mockVerbDisplay());
        st.setObject(mockActivity("testPutStatementNoID"));

        obj.putStatement(st);
    }

    @Test
    public void testGetStatement() throws Exception {
        RemoteLRS obj = getLRS();

        Statement result = obj.getStatement("5bd37f75-db5a-4486-b0fa-b7ec4d82c489");
        log.info("statement: " + result.toJSONPretty());
    }

    private RemoteLRS getLRS() {
        RemoteLRS obj = new RemoteLRS();
        try {
            obj.setEndpoint("http://cloud.scorm.com/ScormEngineInterface/TCAPI/3HYPTQLAI9/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        obj.setVersion(TCAPIVersion.V095);
        obj.setUsername("3HYPTQLAI9");
        obj.setPassword("KJtohao9J76Duu9pNJiOeymxVCjFL0f1EdpbVTFi");

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

    private Verb mockVerb() {
        Verb obj = new Verb();
        try {
            obj.setId(new URL("http://adlnet.gov/expapi/verbs/attempted"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private Verb mockVerbDisplay() {
        Verb obj = mockVerb();
        LanguageMap display = new LanguageMap();
        display.put("und", obj.getId().toString());
        display.put("en-US", "attempted");

        obj.setDisplay(display);

        return obj;
    }

    private Activity mockActivity(String suffix) {
        Activity obj = new Activity();
        try {
            obj.setId(new URL ("http://tincanapi.com/TinCanJava/Test/RemoteLRSTest_mockActivity/" + suffix));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
