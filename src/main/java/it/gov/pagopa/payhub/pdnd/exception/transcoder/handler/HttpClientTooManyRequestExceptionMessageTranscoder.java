package it.gov.pagopa.payhub.pdnd.exception.transcoder.handler;

import it.gov.pagopa.payhub.pdnd.dto.generated.PdndServicesErrorDTO;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoder;
import org.springframework.web.client.HttpClientErrorException;

public class HttpClientTooManyRequestExceptionMessageTranscoder implements ExceptionMessageTranscoder<HttpClientErrorException.TooManyRequests> {
  @Override
  public ExceptionMessageTranscoded transcode(HttpClientErrorException.TooManyRequests tooManyRequestsException) {
    return new ExceptionMessageTranscoded(
      PdndServicesErrorDTO.CategoryEnum.PDND_SERVICES_TOO_MANY_REQUESTS.getValue(),
      tooManyRequestsException.getMessage(),
      null);
  }
}
