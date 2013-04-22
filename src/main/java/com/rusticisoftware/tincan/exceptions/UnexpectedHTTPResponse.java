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
package com.rusticisoftware.tincan.exceptions;

import com.rusticisoftware.tincan.http.HTTPResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UnexpectedHTTPResponse
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UnexpectedHTTPResponse extends RuntimeException {
    private HTTPResponse response;

    public UnexpectedHTTPResponse(String message, HTTPResponse response) {
        super(message);
        this.setResponse(response);
    }

    public UnexpectedHTTPResponse(HTTPResponse response) {
        this("Unexpected HTTP Response", response);
    }

    @Override
    public String toString() {
        HTTPResponse response = this.getResponse();

        return super.toString()
            + ", HTTP status: "
            + response.getStatus()
            + ", HTTP response: "
            + (response.isBinary() ? "binary" : response.getContent().substring(0, (response.getContent().length() > 1000 ? 1000 : response.getContent().length())))
        ;
    }
}
