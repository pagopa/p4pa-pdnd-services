package it.gov.pagopa.payhub.pdnd.exception.custom;

public class JwtClaimBuildException extends RuntimeException {
  public JwtClaimBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
