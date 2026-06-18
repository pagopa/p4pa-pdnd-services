package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.payhub.pdnd.connector.organization.client.PdndServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import org.springframework.stereotype.Service;

@Service
public class PdndServiceServiceImpl implements PdndServiceService {
  private final PdndServiceSearchClient pdndServiceSearchClient;

  public PdndServiceServiceImpl(PdndServiceSearchClient pdndServiceSearchClient) {
    this.pdndServiceSearchClient = pdndServiceSearchClient;
  }

  @Override
  public PdndService findByClientId(String clientId, String accessToken) {
    return pdndServiceSearchClient.findByClientId(clientId, accessToken);
  }
}
