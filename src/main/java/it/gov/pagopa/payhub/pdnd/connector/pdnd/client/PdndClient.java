package it.gov.pagopa.payhub.pdnd.connector.pdnd.client;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;

public interface PdndClient {
  ClientCredentialsResponseDTO getAccessToken(String clientId, String clientAssertions);
}
