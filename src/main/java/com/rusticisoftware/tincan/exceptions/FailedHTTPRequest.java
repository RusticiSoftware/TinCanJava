package com.rusticisoftware.tincan.exceptions;

/**
 * FailedRequest
 */
public class FailedHTTPRequest extends RuntimeException {
    public FailedHTTPRequest(String message) {
        super(message);
    }
}
