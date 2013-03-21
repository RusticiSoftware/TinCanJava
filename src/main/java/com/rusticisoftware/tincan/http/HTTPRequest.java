package com.rusticisoftware.tincan.http;

import lombok.Data;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.io.Buffer;
import java.io.IOException;

/**
 * HTTPRequest Class Description
 */
@Data
public class HTTPRequest extends ContentExchange {
    private HTTPResponse response = new HTTPResponse();

    @Override
    protected void onResponseStatus(Buffer version, int status, Buffer reason) throws IOException {
        super.onResponseStatus(version, status, reason);
        this.response.setStatus(status);
        this.response.setStatusMsg(reason.toString());
    }

    @Override
    protected void onResponseHeader(Buffer name, Buffer value) throws IOException {
        super.onResponseHeader(name, value);

        this.response.setHeader(name.toString(), value.toString());
    }

    @Override
    protected void onResponseComplete() throws IOException {
        super.onResponseComplete();

        this.response.setContentBytes(this.getResponseContentBytes());
    }
}
