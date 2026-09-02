package it.gov.pagopa.payhub.pdnd.exception.transcoder.handler;

import it.gov.pagopa.payhub.pdnd.exception.common.BaseBusinessException;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoder;

public class BaseBusinessExceptionMessageTranscoder implements ExceptionMessageTranscoder<BaseBusinessException> {
  @Override
  public ExceptionMessageTranscoded transcode(BaseBusinessException businessException) {
    return new ExceptionMessageTranscoded(businessException.getCode(), businessException.getMessage(), businessException.getFields());
  }
}
