package it.gov.pagopa.payhub.pdnd.exception.custom;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
