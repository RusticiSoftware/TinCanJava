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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rusticisoftware.tincan.documents.ActivityProfileDocument;
import com.rusticisoftware.tincan.documents.AgentProfileDocument;
import com.rusticisoftware.tincan.documents.Document;
import com.rusticisoftware.tincan.documents.StateDocument;
import com.rusticisoftware.tincan.http.HTTPRequest;
import com.rusticisoftware.tincan.http.HTTPResponse;
import com.rusticisoftware.tincan.lrsresponses.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.*;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpMethod;
import com.rusticisoftware.tincan.exceptions.*;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;
import com.rusticisoftware.tincan.v10x.StatementsQuery;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Class used to communicate with a TCAPI endpoint synchronously
 */
// TODO: handle extended on all requests
@Data
@NoArgsConstructor
public class RemoteLRS implements LRS {
    private static long TIMEOUT_CONNECT = 5 * 1000;

    private static HttpClient _httpClient;
    private static HttpClient httpClient() throws Exception {
        if (_httpClient == null ) {
            _httpClient = new HttpClient(new SslContextFactory());
            _httpClient.setConnectTimeout(TIMEOUT_CONNECT);
            _httpClient.start();
        }

        return _httpClient;
    }

    public static long getHTTPClientConnectTimeout() {
        return _httpClient.getConnectTimeout();
    }
    public static void setHTTPClientConnectTimeout(long timeout) {
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

    private HTTPResponse makeSyncRequest(HTTPRequest req) {
        String url;

        if (req.getResource().toLowerCase().startsWith("http")) {
            url = req.getResource();
        }
        else {
            url = this.endpoint.toString();
            if (! url.endsWith("/") && ! req.getResource().startsWith("/")) {
                url += "/";
            }
            url += req.getResource();
        }

        if (req.getQueryParams() != null) {
            String qs = "";
            Iterator it = req.getQueryParams().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (qs != "") {
                    qs += "&";
                }
                try {
                    qs += URLEncoder.encode(entry.getKey().toString(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {}
            }
            if (qs != "") {
                url += "?" + qs;
            }
        }

        //Overload some of an ContentExchange object's functions with anonymous inner functions
        //Access the HTTPResponse variable via a closure
        final HTTPResponse response = new HTTPResponse();

        try {
            final Request webReq = httpClient().
                    newRequest(url).
                    method(HttpMethod.fromString(req.getMethod())).
                    header("X-Experience-API-Version", this.version.toString()).
                    onResponseHeaders(new Response.HeadersListener() {
                        @Override
                        public void onHeaders(Response webResp) {
                            response.setStatus(webResp.getStatus());
                            response.setStatusMsg(webResp.getReason());
                            for(HttpField header: webResp.getHeaders()) {
                                response.setHeader(header.getName(), header.getValue());
                            }
                        }
                    });

            if (this.auth != null) {
                webReq.header("Authorization", this.auth);
            }
            if (req.getHeaders() != null) {
                Iterator it = req.getHeaders().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    webReq.header(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            if (req.getContentType() != null) {
                webReq.header("Content-Type", req.getContentType());
            }
            else {
                webReq.header("Content-Type", "application/octet-stream");
            }

            if (req.getContent() != null) {
                webReq.content(new BytesContentProvider(req.getContent()));
            }

            final ContentResponse webResp = webReq.send();
            response.setContentBytes(webResp.getContent());
        } catch (Exception ex) {
            response.setStatus(400);
            response.setStatusMsg("Exception in RemoteLRS.makeSyncRequest(): " + ex);
        }

        return response;
    }

    private StatementLRSResponse getStatement(String id, String paramName) {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.GET.asString());
        request.setResource("statements");
        request.setQueryParams(new HashMap<String, String>());
        request.getQueryParams().put(paramName, id);

        HTTPResponse response = makeSyncRequest(request);
        int status = response.getStatus();

        StatementLRSResponse lrsResponse = new StatementLRSResponse(request, response);

        if (status == 200) {
            lrsResponse.setSuccess(true);
            try {
                lrsResponse.setContent(new Statement(new StringOfJSON(response.getContent())));
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    private LRSResponse getDocument(String resource, Map<String, String> queryParams, Document document) {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.GET.asString());
        request.setResource(resource);
        request.setQueryParams(queryParams);

        HTTPResponse response = makeSyncRequest(request);

        LRSResponse lrsResponse = new LRSResponse(request, response);

        if (response.getStatus() == 200) {
            document.setContent(response.getContentBytes());
            document.setContentType(response.getContentType());
            document.setTimestamp(response.getLastModified());
            document.setEtag(response.getEtag());
            lrsResponse.setSuccess(true);
        }
        else if (response.getStatus() == 404) {
            lrsResponse.setSuccess(true);
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    private LRSResponse deleteDocument(String resource, Map<String, String> queryParams) {
        HTTPRequest request = new HTTPRequest();

        request.setMethod(HttpMethod.DELETE.asString());
        request.setResource(resource);
        request.setQueryParams(queryParams);

        HTTPResponse response = makeSyncRequest(request);

        LRSResponse lrsResponse = new LRSResponse(request, response);

        if (response.getStatus() == 204) {
            lrsResponse.setSuccess(true);
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    private LRSResponse saveDocument(String resource, Map<String, String> queryParams, Document document) {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.PUT.asString());
        request.setResource(resource);
        request.setQueryParams(queryParams);
        request.setContentType(document.getContentType());
        request.setContent(document.getContent());
        if (document.getEtag() != null) {
            request.setHeaders(new HashMap<String, String>());
            request.getHeaders().put("If-Match", document.getEtag());
        }

        HTTPResponse response = makeSyncRequest(request);

        LRSResponse lrsResponse = new LRSResponse(request, response);

        if (response.getStatus() == 204) {
            lrsResponse.setSuccess(true);
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    private LRSResponse updateDocument(String resource, Map<String, String> queryParams, Document document) {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.POST.asString());
        request.setResource(resource);
        request.setQueryParams(queryParams);
        request.setContentType(document.getContentType());
        request.setContent(document.getContent());
        if (document.getEtag() != null) {
            request.setHeaders(new HashMap<String, String>());
            request.getHeaders().put("If-Match", document.getEtag());
        }

        HTTPResponse response = makeSyncRequest(request);

        LRSResponse lrsResponse = new LRSResponse(request, response);

        if (response.getStatus() == 204) {
            lrsResponse.setSuccess(true);
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    private ProfileKeysLRSResponse getProfileKeys(String resource, HashMap<String, String> queryParams) {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.GET.asString());
        request.setResource(resource);
        request.setQueryParams(queryParams);

        HTTPResponse response = makeSyncRequest(request);

        ProfileKeysLRSResponse lrsResponse = new ProfileKeysLRSResponse(request, response);

        if (response.getStatus() == 200) {
            lrsResponse.setSuccess(true);
            try {
                Iterator it = Mapper.getInstance().readValue(response.getContent(), ArrayNode.class).elements();

                lrsResponse.setContent(new ArrayList<String>());
                while (it.hasNext()) {
                    lrsResponse.getContent().add(it.next().toString());
                }
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public AboutLRSResponse about() {
        HTTPRequest request = new HTTPRequest();
        request.setMethod(HttpMethod.GET.asString());
        request.setResource("about");

        HTTPResponse response = makeSyncRequest(request);
        int status = response.getStatus();

        AboutLRSResponse lrsResponse = new AboutLRSResponse(request, response);

        if (status == 200) {
            lrsResponse.setSuccess(true);
            try {
                lrsResponse.setContent(new About(response.getContent()));
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public StatementLRSResponse saveStatement(Statement statement) {
        StatementLRSResponse lrsResponse = new StatementLRSResponse();
        lrsResponse.setRequest(new HTTPRequest());

        lrsResponse.getRequest().setResource("statements");
        lrsResponse.getRequest().setContentType("application/json");

        try {
            lrsResponse.getRequest().setContent(statement.toJSON(this.getVersion(), this.usePrettyJSON()).getBytes("UTF-8"));
        } catch (IOException ex) {
            lrsResponse.setErrMsg("Exception: " + ex.toString());
            return lrsResponse;
        }

        if (statement.getId() == null) {
            lrsResponse.getRequest().setMethod(HttpMethod.POST.asString());
        }
        else {
            lrsResponse.getRequest().setMethod(HttpMethod.PUT.asString());
            lrsResponse.getRequest().setQueryParams(new HashMap<String, String>());
            lrsResponse.getRequest().getQueryParams().put("statementId", statement.getId().toString());
        }

        lrsResponse.setResponse(makeSyncRequest(lrsResponse.getRequest()));
        int status = lrsResponse.getResponse().getStatus();

        lrsResponse.setContent(statement);

        // TODO: handle 409 conflict, etc.
        if (status == 200) {
            lrsResponse.setSuccess(true);
            String content = lrsResponse.getResponse().getContent();
            try {
                lrsResponse.getContent().setId(UUID.fromString(Mapper.getInstance().readValue(content, ArrayNode.class).get(0).textValue()));
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else if (status == 204) {
            lrsResponse.setSuccess(true);
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public StatementsResultLRSResponse saveStatements(List<Statement> statements) {
        StatementsResultLRSResponse lrsResponse = new StatementsResultLRSResponse();
        if (statements.size() == 0) {
            lrsResponse.setSuccess(true);
            return lrsResponse;
        }

        ArrayNode rootNode = Mapper.getInstance().createArrayNode();
        for (Statement statement : statements) {
            rootNode.add(statement.toJSONNode(version));
        }

        lrsResponse.setRequest(new HTTPRequest());
        lrsResponse.getRequest().setResource("statements");
        lrsResponse.getRequest().setMethod(HttpMethod.POST.asString());
        lrsResponse.getRequest().setContentType("application/json");
        try {
            lrsResponse.getRequest().setContent(Mapper.getWriter(this.usePrettyJSON()).writeValueAsBytes(rootNode));
        } catch (JsonProcessingException ex) {
            lrsResponse.setErrMsg("Exception: " + ex.toString());
            return lrsResponse;
        }

        HTTPResponse response = makeSyncRequest(lrsResponse.getRequest());
        int status = response.getStatus();

        lrsResponse.setResponse(response);

        if (status == 200) {
            lrsResponse.setSuccess(true);
            lrsResponse.setContent(new StatementsResult());
            try {
                Iterator it = Mapper.getInstance().readValue(response.getContent(), ArrayNode.class).elements();
                for (int i = 0; it.hasNext(); ++i) {
                    lrsResponse.getContent().getStatements().add(statements.get(i));
                    lrsResponse.getContent().getStatements().get(i).setId(UUID.fromString(((JsonNode) it.next()).textValue()));
                }
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public StatementLRSResponse retrieveStatement(String id) {
        return getStatement(id, "statementId");
    }

    @Override
    public StatementLRSResponse retrieveVoidedStatement(String id) {
        String paramName = (this.getVersion() == TCAPIVersion.V095) ? "statementId" : "voidedStatementId";
        return getStatement(id, paramName);
    }

    @Override
    public StatementsResultLRSResponse queryStatements(StatementsQueryInterface query) {
        // Setup empty query object if null was passed in
        if (query == null) {
            query = (this.getVersion() == TCAPIVersion.V095) ?
                new com.rusticisoftware.tincan.v095.StatementsQuery() :
                new StatementsQuery();
        }

        // Choke if the query parameters don't match the LRS version
        if (this.getVersion() != query.getVersion()) {
            throw new IncompatibleTCAPIVersion(
                "Attempted to issue " + this.getVersion() + " query using a " +
                query.getVersion() + " set of query parameters.");
        }

        StatementsResultLRSResponse lrsResponse = new StatementsResultLRSResponse();

        lrsResponse.setRequest(new HTTPRequest());
        lrsResponse.getRequest().setMethod(HttpMethod.GET.asString());
        lrsResponse.getRequest().setResource("statements");

        try {
            lrsResponse.getRequest().setQueryParams(query.toParameterMap());
        } catch (IOException ex) {
            lrsResponse.setErrMsg("Exception: " + ex.toString());
            return lrsResponse;
        }

        HTTPResponse response = makeSyncRequest(lrsResponse.getRequest());

        lrsResponse.setResponse(response);

        if (response.getStatus() == 200) {
            lrsResponse.setSuccess(true);
            try {
                lrsResponse.setContent(new StatementsResult(new StringOfJSON(response.getContent())));
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public StatementsResultLRSResponse moreStatements(String moreURL) {
        if (moreURL == null) {
            return null;
        }

        // moreURL is relative to the endpoint's server root
        URL endpoint = this.getEndpoint();
        String url = endpoint.getProtocol() + "://" + endpoint.getHost() + (endpoint.getPort() == -1 ? "" : ":" + endpoint.getPort()) + moreURL;

        HTTPRequest request = new HTTPRequest();
        request.setResource(url);
        request.setMethod(HttpMethod.GET.asString());
        HTTPResponse response = makeSyncRequest(request);

        StatementsResultLRSResponse lrsResponse = new StatementsResultLRSResponse(request, response);

        if (response.getStatus() == 200) {
            lrsResponse.setSuccess(true);
            try {
                lrsResponse.setContent(new StatementsResult(new StringOfJSON(response.getContent())));
            } catch (Exception ex) {
                lrsResponse.setErrMsg("Exception: " + ex.toString());
                lrsResponse.setSuccess(false);
            }
        }
        else {
            lrsResponse.setSuccess(false);
        }

        return lrsResponse;
    }

    @Override
    public ProfileKeysLRSResponse retrieveStateIds(Activity activity, Agent agent, UUID registration) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("activityId", activity.getId().toString());
        queryParams.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));
        if (registration != null) {
            queryParams.put("registration", registration.toString());
        }

        return getProfileKeys("activities/state", queryParams);
    }

    @Override
    public StateLRSResponse retrieveState(String id, Activity activity, Agent agent, UUID registration) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("stateId", id);
        queryParams.put("activityId", activity.getId().toString());
        queryParams.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));

        StateDocument stateDocument = new StateDocument();
        stateDocument.setId(id);
        stateDocument.setActivity(activity);
        stateDocument.setAgent(agent);

        LRSResponse lrsResp = getDocument("activities/state", queryParams, stateDocument);

        StateLRSResponse lrsResponse = new StateLRSResponse(lrsResp.getRequest(), lrsResp.getResponse());
        lrsResponse.setSuccess(lrsResp.getSuccess());

        if (lrsResponse.getResponse().getStatus() == 200) {
            lrsResponse.setContent(stateDocument);
        }

        return lrsResponse;
    }

    @Override
    public LRSResponse saveState(StateDocument state) {
        HashMap<String,String> queryParams = new HashMap<String,String>();

        queryParams.put("stateId", state.getId());
        queryParams.put("activityId", state.getActivity().getId().toString());
        queryParams.put("agent", state.getAgent().toJSON(this.getVersion(), this.usePrettyJSON()));

        return saveDocument("activities/state", queryParams, state);
    }

    @Override
    public LRSResponse updateState(StateDocument state) {
        HashMap<String,String> queryParams = new HashMap<String,String>();

        queryParams.put("stateId", state.getId());
        queryParams.put("activityId", state.getActivity().getId().toString());
        queryParams.put("agent", state.getAgent().toJSON(this.getVersion(), this.usePrettyJSON()));

        return updateDocument("activities/state", queryParams, state);
    }

    @Override
    public LRSResponse deleteState(StateDocument state) {
        Map queryParams = new HashMap<String, String>();

        queryParams.put("stateId", state.getId());
        queryParams.put("activityId", state.getActivity().getId().toString());
        queryParams.put("agent", state.getAgent().toJSON());

        if (state.getRegistration() != null ) {
            queryParams.put("registration", state.getRegistration().toString());
        }

        return deleteDocument("activities/state", queryParams);
    }

    @Override
    public LRSResponse clearState(Activity activity, Agent agent, UUID registration) {
        HashMap<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("activityId", activity.getId().toString());
        queryParams.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));
        if (registration != null) {
            queryParams.put("registration", registration.toString());
        }
        return deleteDocument("activities/state", queryParams);
    }

    @Override
    public ProfileKeysLRSResponse retrieveActivityProfileIds(Activity activity) {
        HashMap<String, String> queryParams = new HashMap<String, String>();

        queryParams.put("activityId", activity.getId().toString());

        return getProfileKeys("activities/profile", queryParams);
    }

    @Override
    public ActivityProfileLRSResponse retrieveActivityProfile(String id, Activity activity) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", id);
        queryParams.put("activityId", activity.getId().toString());

        ActivityProfileDocument profileDocument = new ActivityProfileDocument();
        profileDocument.setId(id);
        profileDocument.setActivity(activity);

        LRSResponse lrsResp = getDocument("activities/profile", queryParams, profileDocument);

        ActivityProfileLRSResponse lrsResponse = new ActivityProfileLRSResponse(lrsResp.getRequest(), lrsResp.getResponse());
        lrsResponse.setSuccess(lrsResp.getSuccess());

        if (lrsResponse.getResponse().getStatus() == 200) {
            lrsResponse.setContent(profileDocument);
        }

        return lrsResponse;
    }

    @Override
    public LRSResponse saveActivityProfile(ActivityProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("activityId", profile.getActivity().getId().toString());

        return saveDocument("activities/profile", queryParams, profile);
    }

    @Override
    public LRSResponse updateActivityProfile(ActivityProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("activityId", profile.getActivity().getId().toString());

        return updateDocument("activities/profile", queryParams, profile);
    }

    @Override
    public LRSResponse deleteActivityProfile(ActivityProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("activityId", profile.getActivity().getId().toString());
        // TODO: need to pass Etag?

        return deleteDocument("activities/profile", queryParams);
    }

    @Override
    public ProfileKeysLRSResponse retrieveAgentProfileIds(Agent agent) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));

        return getProfileKeys("agents/profile", queryParams);
    }

    @Override
    public AgentProfileLRSResponse retrieveAgentProfile(String id, Agent agent) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", id);
        queryParams.put("agent", agent.toJSON(this.getVersion(), this.usePrettyJSON()));

        AgentProfileDocument profileDocument = new AgentProfileDocument();
        profileDocument.setId(id);
        profileDocument.setAgent(agent);

        LRSResponse lrsResp = getDocument("agents/profile", queryParams, profileDocument);

        AgentProfileLRSResponse lrsResponse = new AgentProfileLRSResponse(lrsResp.getRequest(), lrsResp.getResponse());
        lrsResponse.setSuccess(lrsResp.getSuccess());

        if (lrsResponse.getResponse().getStatus() == 200) {
            lrsResponse.setContent(profileDocument);
        }

        return lrsResponse;
    }

    @Override
    public LRSResponse saveAgentProfile(AgentProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("agent", profile.getAgent().toJSON(this.getVersion(), this.usePrettyJSON()));

        return saveDocument("agents/profile", queryParams, profile);
    }

    @Override
    public LRSResponse updateAgentProfile(AgentProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("agent", profile.getAgent().toJSON(this.getVersion(), this.usePrettyJSON()));

        return updateDocument("agents/profile", queryParams, profile);
    }

    @Override
    public LRSResponse deleteAgentProfile(AgentProfileDocument profile) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("profileId", profile.getId());
        queryParams.put("agent", profile.getAgent().toJSON(this.getVersion(), this.usePrettyJSON()));
        // TODO: need to pass Etag?

        return deleteDocument("agents/profile", queryParams);
    }
}
