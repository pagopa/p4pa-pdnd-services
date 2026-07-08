package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.payhub.pdnd.connector.organization.client.PdndClientClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.stereotype.Service;

@Service
public class PdndClientServiceImpl implements PdndClientService {
  private final PdndClientClient pdndClientClient;

  public PdndClientServiceImpl(PdndClientClient pdndClientClient) {
    this.pdndClientClient = pdndClientClient;
  }

  @Override
  public PdndClientDTO getUsablePdndClientByOrganizationIdAndPdndServiceType(Long organizationId, PdndServiceType serviceType, String subUnitCode, String accessToken) {
    return pdndClientClient.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId, serviceType, subUnitCode, accessToken);
  }
}
