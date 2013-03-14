package tincan;

import lombok.Data;
import lombok.extern.java.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.ByteArrayBuffer;

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
    public Statement fetchStatement(String id) throws IOException, InterruptedException, Exception {
        log.info("fetchStatement - id: " + id);
        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
        exchange.setURL(this.getEndpoint() + "/statements?statementId=" + id);

        httpClient.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = HttpExchange.STATUS_START;
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
    public StatementsResult fetchStatements(List<String> ids) {
        return null;
    }

    @Override
    public StatementsResult queryStatements(StatementsQuery query) {
        return null;
    }

    @Override
    public void saveStatement(Statement statement) throws IOException, InterruptedException {
        log.info("saveStatement: " + statement.toJSONPretty());

        ContentExchange exchange = new ContentExchange();
        exchange.setRequestHeader("Authorization", this.getAuth());
        if (!this.getVersion().equals(TCAPIVersion.V090)) {
            exchange.setRequestHeader("X-Experience-API-Version", this.getVersion().toString());
        }
        exchange.setRequestContentType("application/json");
        exchange.setRequestContent(new ByteArrayBuffer(statement.toJSON(), "UTF-8"));

        String url = this.getEndpoint() + "/statements";
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
    public void saveStatements(Statement[] statements) {
    }
}
