package it.gov.pagopa.payhub.pdnd.config.rest;

import it.gov.pagopa.payhub.pdnd.dto.generated.ErrorFieldDTO;

import java.util.List;

public record PuErrorDTO(
  String category,
  String code,
  String message,
  List<ErrorFieldDTO> fields
) {
}
