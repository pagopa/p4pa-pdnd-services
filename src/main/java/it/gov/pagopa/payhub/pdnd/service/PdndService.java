package it.gov.pagopa.payhub.pdnd.service;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.exception.custom.JwtClaimBuildException;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndBaseServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdndService {

  private final PdndClientImpl pdndClientImpl;
  private final PdndClientAssertionBuilderService pdndClientAssertionBuilderService;
  protected final ConcurrentHashMap<PdndServiceIntegrationConfig, String> jwtCache = new ConcurrentHashMap<>();

  public PdndService(PdndClientImpl pdndClientImpl,
      PdndClientAssertionBuilderService pdndClientAssertionBuilderService) {
    this.pdndClientImpl = pdndClientImpl;
    this.pdndClientAssertionBuilderService = pdndClientAssertionBuilderService;
  }

  public String generateToken(PdndBaseServiceIntegratedConfig pdndBaseServiceIntegratedConfig,
      PdndServiceIntegrationConfig pdndServiceIntegrationConfig) {
    return jwtCache.compute(pdndServiceIntegrationConfig, (key, existingJwt) -> {
      log.debug("Check cache for token exists and not expired for {}", pdndBaseServiceIntegratedConfig.getClass().getName());
      if(existingJwt == null || JWTUtils.isJWTExpired(existingJwt)) {
        try {
          log.debug("Token for {} not present or expired, generate new one", pdndBaseServiceIntegratedConfig.getClass().getName());
          String clientAssertion = pdndClientAssertionBuilderService.buildPdndClientAssertion(pdndBaseServiceIntegratedConfig, key);
          return pdndClientImpl.getAccessToken(pdndBaseServiceIntegratedConfig.getClientId(), clientAssertion).getAccessToken();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException | JOSEException e) {
          throw new JwtClaimBuildException("Error building JWT claims", e);
        }
      }
      log.debug("Token for {} is present in cache", pdndBaseServiceIntegratedConfig.getClass().getName());
      return existingJwt;
    });
  }
}
