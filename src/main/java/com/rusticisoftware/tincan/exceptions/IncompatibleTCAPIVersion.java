package com.rusticisoftware.tincan.exceptions;

/**
 * IncompatibleTCAPIVersion
 */
public class IncompatibleTCAPIVersion extends RuntimeException {
    public IncompatibleTCAPIVersion(String message) {
        super(message);
    }
}
