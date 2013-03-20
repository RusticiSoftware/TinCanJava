package com.rusticisoftware.tincan.exceptions;

import lombok.Data;

/**
 * UnexpectedHTTPResponse
 */
@Data
public class UnexpectedHTTPResponse extends RuntimeException {
    private int status;
    private byte[] response;

    public UnexpectedHTTPResponse(String message, int status, byte[] response) {
        super(message);
        this.setStatus(status);
        this.setResponse(response);
    }

    public UnexpectedHTTPResponse(int status, byte[] response) {
        this("Unexpected HTTP Response", status, response);
    }
}
