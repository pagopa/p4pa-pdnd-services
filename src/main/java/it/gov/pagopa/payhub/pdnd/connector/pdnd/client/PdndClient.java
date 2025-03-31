package it.gov.pagopa.payhub.pdnd.connector.pdnd.client;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApisHolder;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.api.AuthApi;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class PdndClient {

  private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
  private static final String GRANT_TYPE = "client_credentials";

  private final AuthApi authApi;

  public PdndClient(PdndApisHolder pdndApisHolder) {
    authApi = pdndApisHolder.getBrokerEntityControllerApi();
  }

  public ClientCredentialsResponseDTO getAccessToken(String clientId, String clientAssertion) {
    return authApi.createToken(clientAssertion, CLIENT_ASSERTION_TYPE, GRANT_TYPE, clientId);
  }
}
