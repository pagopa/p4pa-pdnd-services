package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class PdndServiceSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public PdndServiceSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PdndService findByClientId(String clientId, String accessToken) {
    try {
      return organizationApisHolder.getPdndServiceSearchControllerApi(accessToken)
        .crudPdndServicesFindByClientId(clientId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("PdndService with clientId {} not found", clientId);
      return null;
    }
  }
}
