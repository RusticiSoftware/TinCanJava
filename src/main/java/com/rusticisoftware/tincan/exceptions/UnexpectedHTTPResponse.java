package com.rusticisoftware.tincan.exceptions;

import lombok.Data;

/**
 * UnexpectedHTTPResponse
 */
@Data
public class UnexpectedHTTPResponse extends RuntimeException {
    private int status;
    private String response;

    public UnexpectedHTTPResponse(String message, int status, String response) {
        super(message);
        this.setStatus(status);
        this.setResponse(response);
    }

    public UnexpectedHTTPResponse(int status, String response) {
        super("Unexpected HTTP Response");
        this.setStatus(status);
        this.setResponse(response);
    }
}
