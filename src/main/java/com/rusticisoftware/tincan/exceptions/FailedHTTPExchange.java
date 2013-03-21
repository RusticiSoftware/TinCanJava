package com.rusticisoftware.tincan.exceptions;

import lombok.Data;

/**
 * FailedHTTPExchange
 */
@Data
public class FailedHTTPExchange extends RuntimeException {
    private int status;

    public FailedHTTPExchange(String message, int status) {
        super(message);
        this.setStatus(status);
    }
    public FailedHTTPExchange(int status) {
        this("Failed HTTP Exchange", status);
    }
}
