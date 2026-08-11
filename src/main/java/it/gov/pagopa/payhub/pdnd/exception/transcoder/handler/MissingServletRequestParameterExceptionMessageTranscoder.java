package it.gov.pagopa.payhub.pdnd.exception.transcoder.handler;

import it.gov.pagopa.payhub.pdnd.dto.generated.PdndServicesErrorDTO;
import it.gov.pagopa.payhub.pdnd.dto.generated.ErrorFieldDTO;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.payhub.pdnd.exception.transcoder.ExceptionMessageTranscoder;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

public class MissingServletRequestParameterExceptionMessageTranscoder implements ExceptionMessageTranscoder<MissingServletRequestParameterException> {

  @Override
  public ExceptionMessageTranscoded transcode(MissingServletRequestParameterException missingServletRequestParameterException) {
    return new ExceptionMessageTranscoded(
      PdndServicesErrorDTO.CategoryEnum.PDND_SERVICES_BAD_REQUEST.getValue(),
      missingServletRequestParameterException.getMessage(),
      List.of(new ErrorFieldDTO(missingServletRequestParameterException.getParameterName(), "NotNull", missingServletRequestParameterException.getMessage())));
  }
}
