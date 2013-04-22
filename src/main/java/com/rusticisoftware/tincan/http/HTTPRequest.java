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
package com.rusticisoftware.tincan.http;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.io.Buffer;
import java.io.IOException;

/**
 * HTTPRequest Class Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
