package com.rusticisoftware.tincan.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * FailedHTTPExchange
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
