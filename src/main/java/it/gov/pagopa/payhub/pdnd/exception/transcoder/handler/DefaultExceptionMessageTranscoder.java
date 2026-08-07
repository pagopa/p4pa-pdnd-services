package it.gov.pagopa.payhub.pdnd.exception.transcoder.handler;

import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoder;
import org.apache.hc.client5.http.HttpHostConnectException;

public class DefaultExceptionMessageTranscoder implements ExceptionMessageTranscoder<Exception> {
  @Override
  public ExceptionMessageTranscoded transcode(Exception exception) {
    if (exception.getCause() instanceof HttpHostConnectException) {
      return new ExceptionMessageTranscoded("PDND_SERVICES_CONNECTION_ERROR", exception.getMessage(), null);
    }
    return new ExceptionMessageTranscoded(null, exception.getMessage(), null);
  }
}
