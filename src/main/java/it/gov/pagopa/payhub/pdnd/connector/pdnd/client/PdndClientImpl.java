package it.gov.pagopa.payhub.pdnd.connector.pdnd.client;

import it.gov.pagopa.common.pdnd.generated.ApiClient;
import it.gov.pagopa.common.pdnd.generated.api.AuthApi;
import it.gov.pagopa.common.pdnd.generated.dto.ClientCredentialsResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PdndClientImpl implements PdndClient {

  private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
  private static final String GRANT_TYPE = "client_credentials";
  private final AuthApi authApi;

  public PdndClientImpl(RestTemplateBuilder restTemplateBuilder,
      @Value("${app.pdnd.base-url}") String pdndBaseUrl) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(pdndBaseUrl);
    authApi = new AuthApi(apiClient);
  }

  @Override
  public ClientCredentialsResponseDTO getAccessToken(String clientId, String clientAssertions) {
    return authApi.createToken(clientAssertions, CLIENT_ASSERTION_TYPE, GRANT_TYPE, clientId);
  }
}
