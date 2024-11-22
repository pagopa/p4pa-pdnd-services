package it.gov.pagopa.payhub.pdnd.service;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.exception.custom.JwtClaimBuildException;
import it.gov.pagopa.payhub.pdnd.model.PdndGenericConfig;
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
  private final ConcurrentHashMap<PdndGenericConfig, String> jwtCache = new ConcurrentHashMap<>();

  public PdndService(PdndClientImpl pdndClientImpl,
      PdndClientAssertionBuilderService pdndClientAssertionBuilderService) {
    this.pdndClientImpl = pdndClientImpl;
    this.pdndClientAssertionBuilderService = pdndClientAssertionBuilderService;
  }

  public String generateToken(PdndGenericConfig pdndGenericConfig) {
    return jwtCache.compute(pdndGenericConfig, (key, existingJwt) -> {
      log.debug("Check cache for token exists and not expired");
      if(existingJwt == null || JWTUtils.isJWTExpired(existingJwt)) {
        try {
          log.debug("Token not present or expired, generate new one");
          String clientAssertion = pdndClientAssertionBuilderService.buildPdndClientAssertion(key);
          return pdndClientImpl.getAccessToken(key.getClientId(), clientAssertion).getAccessToken();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException | JOSEException e) {
          throw new JwtClaimBuildException("Error building JWT claims", e);
        }
      }
      log.debug("Token is present in cache");
      return existingJwt;
    });
  }
}
