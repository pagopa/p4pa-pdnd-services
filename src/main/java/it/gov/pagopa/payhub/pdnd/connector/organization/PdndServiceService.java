package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndService;

public interface PdndServiceService {
  PdndService findByClientId(String clientId, String accessToken);
}
