package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.dto.AccessTokenDTO;

public interface PdndClient {
  AccessTokenDTO getAccessToken() throws Exception;
}
