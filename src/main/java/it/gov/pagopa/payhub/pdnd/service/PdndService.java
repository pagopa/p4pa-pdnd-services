package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdndService {

  private final PdndClientImpl pdndClientImpl;
  private final PdndClientAssertionBuilderService pdndClientAssertionBuilderService;
  protected final ConcurrentHashMap<PdndServiceIntegratedConfig, String> jwtCache = new ConcurrentHashMap<>();

  public PdndService(PdndClientImpl pdndClientImpl,
      PdndClientAssertionBuilderService pdndClientAssertionBuilderService) {
    this.pdndClientImpl = pdndClientImpl;
    this.pdndClientAssertionBuilderService = pdndClientAssertionBuilderService;
  }

  public String generateToken(PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
    return jwtCache.compute(pdndServiceIntegratedConfig, (key, existingJwt) -> {
      log.debug("Check cache for token exists and not expired for {}", pdndServiceIntegratedConfig.getClass().getName());
      if(existingJwt == null || JWTUtils.isJWTExpired(existingJwt)) {
          log.debug("Token for {} not present or expired, generate new one", pdndServiceIntegratedConfig.getClass().getName());
          String clientAssertion = pdndClientAssertionBuilderService.buildPdndClientAssertion(key);
          return pdndClientImpl.getAccessToken(pdndServiceIntegratedConfig.getClientId(), clientAssertion).getAccessToken();
      }
      log.debug("Token for {} is present in cache", pdndServiceIntegratedConfig.getClass().getName());
      return existingJwt;
    });
  }
}
