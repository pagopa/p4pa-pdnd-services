package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;

public interface PdndServiceService {
  PdndService findByClientIdAndServiceType(String clientId, PdndServiceType pdndServiceType, String accessToken);
}
