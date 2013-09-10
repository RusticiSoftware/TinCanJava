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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rusticisoftware.tincan.http.HTTPRequest;
import com.rusticisoftware.tincan.http.HTTPResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.ByteArrayBuffer;
import com.rusticisoftware.tincan.exceptions.*;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;
import com.rusticisoftware.tincan.v10x.StatementsQuery;

import static org.eclipse.jetty.client.HttpClient.CONNECTOR_SELECT_CHANNEL;

/**
 * Class used to communicate with a TCAPI endpoint synchronously
 */
// TODO: handle extended on all requests
@Data
@NoArgsConstructor
public class RemoteLRS implements LRS {
    private static int TIMEOUT_CONNECT = 5 * 1000;

    private static HttpClient _httpClient;
    private static HttpClient httpClient() throws Exception {
        if (_httpClient == null ) {
            _httpClient = new HttpClient();
            _httpClient.setConnectorType(CONNECTOR_SELECT_CHANNEL);
            _httpClient.setConnectTimeout(TIMEOUT_CONNECT);
            _httpClient.start();
        }

        return _httpClient;
    }

    public static int getHTTPClientConnectTimeout() {
        return _httpClient.getConnectTimeout();
    }
    public static void setHTTPClientConnectTimeout(int timeout) {
        _httpClient.setConnectTimeout(timeout);
    }

    private URL endpoint;
    private TCAPIVersion version = this.getVersion();
    private String username;
    private String password;
    private String auth;
    private HashMap extended;
    private Boolean prettyJSON = false;

    public RemoteLRS(TCAPIVersion version) {
        this.setVersion(version);
    }

    // MD - added a useful ctor
    public RemoteLRS(TCAPIVersion version, String _endpoint, String _username, String _password) throws MalformedURLException
    {
    	this.setVersion(version);
    	this.setEndpoint(new URL(_endpoint));
    	this.setPassword(_password);
    	this.setUsername(_username);
    	
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

    /**
     * Alternate Getter method for readability of code
     */
    public Boolean usePrettyJSON() {
        return this.getPrettyJSON();
    }

    public String calculateBasicAuth() {
        return  "Basic " + Base64.encodeBase64String(
            (this.getUsername() + ":" + this.getPassword()).getBytes()
        );
    }

    private HTTPResponse sendRequest(HTTPRequest request) throws Exception {
        request.setRequestHeader("Authorization", this.getAuth());
        request.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        request.setRequestContentType("application/json");

        httpClient().send(request);

        // Waits until the exchange is terminated
        int exchangeState = request.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            return request.getResponse();
        }
        throw new FailedHTTPExchange(exchangeState);
    }

    @Override
    public Statement retrieveStatement(String id) throws Exception {
        return retrieveStatement(id, "statementId");
    }
    
    private Statement retrieveStatement(String id, String paramName) throws Exception {
        HTTPRequest request = new HTTPRequest();
        request.setURL(this.getEndpoint() + "statements?" + paramName + "=" + id);

        HTTPResponse response = this.sendRequest(request);
        int status = response.getStatus();

        if (status == 200) {
            return new Statement(new StringOfJSON(response.getContent()));
        }
        else if (status == 404) {
            return null;
        }
        throw new UnexpectedHTTPResponse(response);
    }

    @Override
    public StatementsResult queryStatements(StatementsQueryInterface query) throws Exception {
        //Setup empty query object if null was passed in
        if (query == null) { 
            query = (this.getVersion() == TCAPIVersion.V095) ? 
                        new com.rusticisoftware.tincan.v095.StatementsQuery() :
                        new StatementsQuery();
        }
        
        //Choke if the query parameters don't match the LRS version
        if (this.getVersion() != query.getVersion()) {
            throw new IncompatibleTCAPIVersion(
                    "Attempted to issue " + this.getVersion() + " query using a " +
                    query.getVersion() + " set of query parameters.");
        }

        //Build query string
        HashMap<String,String> params = query.toParameterMap();
        StringBuilder queryString = new StringBuilder();
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString.append((first ? "?" : "&") + 
                URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + 
                URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20"));
            first = false;
        }

        HTTPRequest request = new HTTPRequest();
        request.setURL(this.getEndpoint() + "statements" + queryString.toString());

        HTTPResponse response = this.sendRequest(request);
        if (response.getStatus() == 200) {
            return new StatementsResult(new StringOfJSON(response.getContent()));
        }

        throw new UnexpectedHTTPResponse(response);
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

        HTTPRequest request = new HTTPRequest();
        request.setURL(url);

        HTTPResponse response = this.sendRequest(request);
        if (response.getStatus() == 200) {
            return new StatementsResult(new StringOfJSON(response.getContent()));
        }

        throw new UnexpectedHTTPResponse(response);
    }

    @Override
    public UUID saveStatement(Statement statement) throws Exception {
        HTTPRequest request = new HTTPRequest();
        request.setRequestContent(new ByteArrayBuffer(statement.toJSON(this.getVersion(), this.usePrettyJSON()), "UTF-8"));

        String url = this.getEndpoint() + "statements";
        if (statement.getId() == null) {
            request.setMethod(HttpMethods.POST);
        }
        else {
            request.setMethod(HttpMethods.PUT);
            url += "?statementId=" + statement.getId().toString();
        }
        request.setURL(url);

        HTTPResponse response = this.sendRequest(request);
        int status = response.getStatus();

        // TODO: handle 409 conflict, etc.
        if (status == 204) {
            return statement.getId();
        }
        else if (status == 200) {
            String content = request.getResponseContent();
            return UUID.fromString(Mapper.getInstance().readValue(content, ArrayNode.class).get(0).textValue());
        }

        throw new UnexpectedHTTPResponse(response);
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

        HTTPRequest request = new HTTPRequest();
        request.setRequestContent(new ByteArrayBuffer(Mapper.getWriter(this.usePrettyJSON()).writeValueAsString(rootNode), "UTF-8"));
        request.setMethod(HttpMethods.POST);
        request.setURL(this.getEndpoint() + "statements");

        HTTPResponse response = this.sendRequest(request);
        int status = response.getStatus();

        if (status == 200) {
            String content = request.getResponseContent();
            Iterator it =  Mapper.getInstance().readValue(content, ArrayNode.class).elements();
            while(it.hasNext()) {
                statementIds.add( ((JsonNode) it.next()).textValue() );
            }
            return statementIds;
        }

        throw new UnexpectedHTTPResponse(response);
    }

    @Override
    public State retrieveState(String id, String activityId, Agent agent, UUID registration) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("stateId", id);
        params.put("activityId", activityId);
        params.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));
        if (registration != null) {
            params.put("registration", registration.toString());
        }

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
            first = false;
        }

        HTTPRequest request = new HTTPRequest();
        request.setURL(this.getEndpoint() + "activities/state" + queryString);

        HTTPResponse response = this.sendRequest(request);
        int status = response.getStatus();

        if (status == 200) {
            return new State(id, response.getContentBytes(), activityId, agent, registration);
        }
        else if (status == 404) {
            return null;
        }
        throw new UnexpectedHTTPResponse(response);
    }

    @Override
    public void saveState(State state, String activityId, Agent agent, UUID registration) throws Exception {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("stateId", state.getId());
        params.put("activityId", activityId);
        params.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));
        if (registration != null) {
            params.put("registration", registration.toString());
        }

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
            first = false;
        }

        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethods.PUT);
        request.setURL(this.getEndpoint() + "activities/state" + queryString);
        request.setRequestContent(new ByteArrayBuffer(state.getContents()));

        // TODO: need to set the 'updated' property based on header
        HTTPResponse response = this.sendRequest(request);
        int status = response.getStatus();

        if (status == 204) {
            return;
        }
        throw new UnexpectedHTTPResponse(response);
    }

    @Override
    public Statement retrieveVoidedStatement(String id) throws Exception {
        String paramName = (this.getVersion() == TCAPIVersion.V095) ? "statementId" : "voidedStatementId";
        return retrieveStatement(id, paramName);
    }
}
