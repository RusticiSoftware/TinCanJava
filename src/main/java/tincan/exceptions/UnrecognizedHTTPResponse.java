package tincan.exceptions;

/**
 * FailedRequest
 */
public class UnrecognizedHTTPResponse extends RuntimeException {
    public UnrecognizedHTTPResponse(String message) {
        super(message);
    }
}
