package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;

public interface PdndClientService {
  PdndClientDTO getUsablePdndClientByOrganizationIdAndPdndServiceType(Long organizationId, PdndServiceType serviceType, String subUnitCode, String accessToken);
}
