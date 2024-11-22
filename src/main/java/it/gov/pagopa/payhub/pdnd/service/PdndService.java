package it.gov.pagopa.payhub.pdnd.service;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.model.PdndGenericConfig;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.stereotype.Service;

@Service
public class PdndService {

  private final PdndClientImpl pdndClientImpl;
  private final PdndClientAssertionBuilderService pdndClientAssertionBuilderService;

  public PdndService(PdndClientImpl pdndClientImpl,
      PdndClientAssertionBuilderService pdndClientAssertionBuilderService) {
    this.pdndClientImpl = pdndClientImpl;
    this.pdndClientAssertionBuilderService = pdndClientAssertionBuilderService;
  }

  public String generateToken(PdndGenericConfig pdndGenericConfig)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    String clientAssertion = pdndClientAssertionBuilderService.buildPdndClientAssertion(pdndGenericConfig);
    return pdndClientImpl.getAccessToken(pdndGenericConfig.getClientId(), clientAssertion).getAccessToken();
  }
}
