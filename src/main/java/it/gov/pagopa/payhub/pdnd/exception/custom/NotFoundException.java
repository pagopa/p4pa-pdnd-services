package it.gov.pagopa.payhub.pdnd.exception.custom;

import it.gov.pagopa.payhub.pdnd.exception.BaseBusinessException;

public class NotFoundException extends BaseBusinessException {
  public NotFoundException(String code, String message) {
    this(code, message, null);
  }

  public NotFoundException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
