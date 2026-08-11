package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.payhub.pdnd.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdndServiceSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public PdndServiceSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PdndService findByClientIdAndServiceType(String clientId, PdndServiceType pdndServiceType, String accessToken) {
    try {
      return organizationApisHolder.getPdndServiceSearchControllerApi(accessToken)
        .crudPdndServicesFindByClientIdAndServiceType(clientId, pdndServiceType);
    } catch (RestInvokeNotFoundException e) {
      log.warn("PdndService with clientId {} and serviceType {} not found", clientId, pdndServiceType);
      return null;
    }
  }
}
