package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.ByteArrayBuffer;
import com.rusticisoftware.tincan.exceptions.*;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;

import static org.eclipse.jetty.client.HttpClient.CONNECTOR_SELECT_CHANNEL;

/**
 * Class used to communicate with a TCAPI endpoint synchronously
 */
// TODO: handle extended on all requests
@Data
@NoArgsConstructor
public class RemoteLRS implements LRS {
    private static HttpClient _httpClient;
    private static HttpClient httpClient() throws Exception {
        if (_httpClient == null ) {
            _httpClient = new HttpClient();
            _httpClient.setConnectorType(CONNECTOR_SELECT_CHANNEL);
            _httpClient.start();
        }

        return _httpClient;
    }

    private URL endpoint;
    private TCAPIVersion version = this.getVersion();
    private String username;
    private String password;
    private String auth;
    private HashMap extended;

    public RemoteLRS(TCAPIVersion version) {
        this.setVersion(version);
    }

    private void setEndpoint(URL url) throws MalformedURLException {
        String strUrl = url.toString();
        if (! strUrl.substring(strUrl.length() - 1).equals("/")) {
            strUrl += "/";
            url = new URL(strUrl);
        }
        this.endpoint = url;
    }

    public void setEndpoint(String str) throws MalformedURLException {
        this.setEndpoint(new URL(str));
    }

    public void setUsername(String str) {
        this.username = str;

        if (this.password != null) {
            this.setAuth(this.calculateBasicAuth());
        }
    }

    public void setPassword(String str) {
        this.password = str;

        if (this.username != null) {
            this.setAuth(this.calculateBasicAuth());
        }
    }

    public String calculateBasicAuth() {
        return  "Basic " + Base64.encodeBase64String(
            (this.getUsername() + ":" + this.getPassword()).getBytes()
        );
    }

    private HTTPResponse makeRequest(ContentExchange exchange) throws Exception {
        // TODO: add handling of extended parameters
        exchange.setRequestHeader("Authorization", this.getAuth());
        exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        exchange.setRequestContentType("application/json");

        httpClient().send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            return new HTTPResponse(exchange.getResponseStatus(), exchange.getResponseContent());
        }
        else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
            throw new FailedHTTPRequest("exchange status excepted");
        }
        else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
            throw new FailedHTTPRequest("exchange status expired");
        }
        throw new FailedHTTPRequest("exchange status unrecognized");
    }

    @Override
    public Statement retrieveStatement(String id) throws Exception {
        ContentExchange exchange = new ContentExchange();
        exchange.setURL(this.getEndpoint() + "statements?statementId=" + id);

        HTTPResponse response = this.makeRequest(exchange);
        int status = response.getStatus();

        if (status == 200) {
            return new Statement(new StringOfJSON(response.getContent()));
        }
        else if (status == 404) {
            return null;
        }
        throw new UnexpectedHTTPResponse(status, response.getContent());
    }

    @Override
    public StatementsResult queryStatements(StatementsQuery query) throws Exception {
        if (query == null) {
            query = new StatementsQuery();
        }

        HashMap<String,String> params = query.toParameterMap(this.getVersion());

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
            first = false;
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setURL(this.getEndpoint() + "statements" + queryString);

        HTTPResponse response = this.makeRequest(exchange);
        if (response.getStatus() == 200) {
            return new StatementsResult(new StringOfJSON(response.getContent()));
        }

        throw new UnexpectedHTTPResponse(response.getStatus(), response.getContent());
    }

    @Override
    public StatementsResult moreStatements(String moreURL) throws Exception {
        if (moreURL == null) {
            return null;
        }

        //
        // moreURL is relative to the endpoint's server root
        //
        URL endpoint = this.getEndpoint();
        String url = endpoint.getProtocol() + "://" + endpoint.getHost() + (endpoint.getPort() == -1 ? "" : endpoint.getPort()) + moreURL;

        ContentExchange exchange = new ContentExchange();
        exchange.setURL(url);

        HTTPResponse response = this.makeRequest(exchange);
        if (response.getStatus() == 200) {
            return new StatementsResult(new StringOfJSON(response.getContent()));
        }

        throw new UnexpectedHTTPResponse(response.getStatus(), response.getContent());
    }

    @Override
    public UUID saveStatement(Statement statement) throws Exception {
        ContentExchange exchange = new ContentExchange();
        exchange.setRequestContent(new ByteArrayBuffer(statement.toJSON(), "UTF-8"));

        String url = this.getEndpoint() + "statements";
        if (statement.getId() == null) {
            exchange.setMethod(HttpMethods.POST);
        }
        else {
            exchange.setMethod(HttpMethods.PUT);
            url += "?statementId=" + statement.getId().toString();
        }
        exchange.setURL(url);

        HTTPResponse response = this.makeRequest(exchange);
        int status = response.getStatus();

        // TODO: handle 409 conflict, etc.
        if (status == 204) {
            return statement.getId();
        }
        else if (status == 200) {
            String content = exchange.getResponseContent();
            return UUID.fromString(Mapper.getInstance().readValue(content, ArrayNode.class).get(0).textValue());
        }

        throw new UnexpectedHTTPResponse(status, response.getContent());
    }

    @Override
    public List<String> saveStatements(List<Statement> statements) throws Exception {
        List<String> statementIds = new ArrayList<String>();
        if (statements.size() == 0) {
            return statementIds;
        }

        ArrayNode rootNode = Mapper.getInstance().createArrayNode();
        for (Statement statement : statements) {
            rootNode.add(statement.toJSONNode(version));
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setRequestContent(new ByteArrayBuffer(Mapper.getInstance().writeValueAsString(rootNode), "UTF-8"));
        exchange.setMethod(HttpMethods.POST);
        exchange.setURL(this.getEndpoint() + "statements");

        HTTPResponse response = this.makeRequest(exchange);
        int status = response.getStatus();

        if (status == 200) {
            String content = exchange.getResponseContent();
            Iterator it =  Mapper.getInstance().readValue(content, ArrayNode.class).elements();
            while(it.hasNext()) {
                statementIds.add( ((JsonNode) it.next()).textValue() );
            }
            return statementIds;
        }

        throw new UnexpectedHTTPResponse(status, response.getContent());
    }

    @Override
    public State retrieveState(String id, String activityId, Agent agent, UUID registration) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("stateId", id);
        params.put("activityId", activityId);
        params.put("agent", agent.toJSON(this.getVersion()));
        if (registration != null) {
            params.put("registration", registration.toString());
        }

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
            first = false;
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setURL(this.getEndpoint() + "activities/state" + queryString);

        HTTPResponse response = this.makeRequest(exchange);
        int status = response.getStatus();

        if (status == 200) {
            return new State(id, response.getContent(), activityId, agent, registration);
        }
        else if (status == 404) {
            return null;
        }
        throw new UnexpectedHTTPResponse(status, response.getContent());
    }

    @Override
    public void saveState(State state, String activityId, Agent agent, UUID registration) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("stateId", state.getId());
        params.put("activityId", activityId);
        params.put("agent", agent.toJSON(this.getVersion()));
        if (registration != null) {
            params.put("registration", registration.toString());
        }

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
            first = false;
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setMethod(HttpMethods.PUT);
        exchange.setURL(this.getEndpoint() + "activities/state" + queryString);
        exchange.setRequestContent(new ByteArrayBuffer(state.getContents()));

        // TODO: need to set the 'updated' property based on header
        HTTPResponse response = this.makeRequest(exchange);
        int status = response.getStatus();

        if (status == 204) {
            return;
        }
        throw new UnexpectedHTTPResponse(status, response.getContent());
    }

    protected class HTTPResponse extends HashMap<String,Object> {
        private int status;
        private String content;

        public HTTPResponse() {}
        public HTTPResponse(int status, String content) {
            this.status = status;
            this.content = content;
        }
        public int getStatus() {return this.status;}
        public void setStatus(int status) {this.status = status;}
        public String getContent() {return this.content;}
        public void setContent(String content) {this.content = content;}
    }
}
