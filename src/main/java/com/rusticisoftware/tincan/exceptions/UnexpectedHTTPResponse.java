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
