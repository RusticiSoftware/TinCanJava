package tincan;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.java.Log;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import tincan.json.Mapper;
import tincan.json.StringOfJSON;

import static org.eclipse.jetty.client.HttpClient.CONNECTOR_SELECT_CHANNEL;

/**
 * Class used to communicate with a TCAPI endpoint synchronously
 */
// TODO: handle extended on all requests
@Data
@Log
public class RemoteLRS implements LRS {
    private static HttpClient httpClient = new HttpClient();
    // TODO: should this be an instance private?
    static {
        httpClient.setConnectorType(CONNECTOR_SELECT_CHANNEL);
        try {
            httpClient.start();
        } catch (Exception e) {
            // TODO: what should this be?
            e.printStackTrace();
        }
    }

    private URL endpoint;
    private TCAPIVersion version;
    private String username;
    private String password;
    private String auth;
    private HashMap extended;

    private void setEndpoint(URL url) {
        String strUrl = url.toString();
        if (! strUrl.substring(strUrl.length() - 1).equals("/")) {
            try {
                strUrl += "/";
                url = new URL(strUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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

    @Override
    public Statement fetchStatement(String id) throws Exception {
        log.info("fetchStatement - id: " + id);
        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
        exchange.setURL(this.getEndpoint() + "statements?statementId=" + id);

        httpClient.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState;
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            log.info("exchange completed");
            String content = exchange.getResponseContent();

            return new Statement(content);
        }
        else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
            log.severe("exchange failed");
        }
        else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
            log.warning("exchange expired");
        }
        else {
            log.severe("exchange returned unhandled status");
        }

        return null;
    }

    @Override
    public StatementsResult queryStatements(StatementsQuery query) throws Exception {
        log.info("queryStatements");
        if (query == null) {
            query = new StatementsQuery();
        }

        HashMap<String,String> params = new HashMap<String,String>();
        if (query.getVerb() != null) {
            params.put("verb", query.getVerb().getId().toString());
        }
        else if (query.getVerbID() != null) {
            params.put("verb", query.getVerbID());
        }
        if (query.getObject() != null) {
            params.put("object", query.getObject().toJSON(this.getVersion()));
        }
        if (query.getRegistration() != null) {
            params.put("registration", query.getRegistration().toString());
        }
        if (query.getContext() != null) {
            params.put("context", query.getContext().toString());
        }
        if (query.getActor() != null) {
            params.put("actor", query.getActor().toJSON(this.getVersion()));
        }
        if (query.getSince() != null) {
            //
            // The following was giving ZZ which has a : in it which was blowing up
            //params.put("since", query.getSince().toString(ISODateTimeFormat.dateTime()));
            //
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            params.put("since", query.getSince().toString(fmt));
        }
        if (query.getUntil() != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            params.put("until", query.getUntil().toString(fmt));
        }
        if (query.getLimit() != null) {
            params.put("limit", query.getLimit().toString());
        }
        if (query.getAuthoritative() != null) {
            params.put("authoritative", query.getAuthoritative().toString());
        }
        if (query.getSparse() != null) {
            params.put("sparse", query.getSparse().toString());
        }
        if (query.getInstructor() != null) {
            params.put("instructor", query.getInstructor().toJSON(this.getVersion()));
        }
        if (query.getAscending() != null) {
            params.put("ascending", query.getAscending().toString());
        }

        String queryString = "?";
        Boolean first = true;
        for(Map.Entry<String,String> parameter : params.entrySet()) {
            try {
                queryString += (first ? "" : "&") + URLEncoder.encode(parameter.getKey(), "UTF-8") + "=" + URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+", "%20");
                first = false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
        exchange.setURL(this.getEndpoint() + "statements" + queryString);

        log.info("exchange url: " + exchange.getRequestURI());

        httpClient.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState;
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            log.info("exchange completed");
            log.info("exchange return status: " + exchange.getResponseStatus());
            if (exchange.getResponseStatus() >= 200 && exchange.getResponseStatus() < 300) {
                StringOfJSON content = new StringOfJSON(exchange.getResponseContent());
                return new StatementsResult(content);
            }
            else {
                log.info("exchange content: " + exchange.getResponseContent());
            }
        }
        else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
            log.severe("exchange failed");
        }
        else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
            log.warning("exchange expired");
        }
        else {
            log.severe("exchange returned unhandled status");
        }
        return null;
    }

    @Override
    public void saveStatement(Statement statement) throws Exception {
        log.info("saveStatement: " + statement.toJSONPretty());

        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
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

        httpClient.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = HttpExchange.STATUS_START;
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            log.info("exchange completed");
            log.info("status: " + exchange.getResponseStatus());

            String content = exchange.getResponseContent();
            log.info("content: " + content);
        }
        else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
            log.severe("exchange failed");
        }
        else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
            log.warning("exchange expired");
        }
        else {
            log.severe("exchange returned unhandled status");
        }
    }

    @Override
    public void saveStatements(Statement[] statements) throws Exception {
        log.info("saveStatements: " + statements.length);
        if (statements.length == 0) {
            return;
        }

        ArrayNode rootNode = Mapper.getInstance().createArrayNode();
        for (Statement statement : statements) {
            rootNode.add(statement.toJSONNode(version));
        }

        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
        exchange.setRequestContent(new ByteArrayBuffer(Mapper.getInstance().writeValueAsString(rootNode), "UTF-8"));

        exchange.setMethod(HttpMethods.POST);
        exchange.setURL(this.getEndpoint() + "statements");

        httpClient.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = HttpExchange.STATUS_START;
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            log.info("exchange completed");
            log.info("status: " + exchange.getResponseStatus());

            String content = exchange.getResponseContent();
            log.info("content: " + content);
        }
        else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
            log.severe("exchange failed");
        }
        else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
            log.warning("exchange expired");
        }
        else {
            log.severe("exchange returned unhandled status");
        }
    }
}
