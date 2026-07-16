package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class PdndClientClient {

  private final OrganizationApisHolder organizationApisHolder;

  public PdndClientClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PdndClientDTO getUsablePdndClientByOrganizationIdAndPdndServiceType(Long organizationId, PdndServiceType serviceType, String subUnitCode, String accessToken) {
    try {
      return organizationApisHolder.getPdndClientApi(accessToken)
              .getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId, serviceType, subUnitCode);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("PdndClient with organizationId {} serviceType {} and subUnitCode {} not found", organizationId, serviceType, subUnitCode);
      return null;
    }
  }
}
