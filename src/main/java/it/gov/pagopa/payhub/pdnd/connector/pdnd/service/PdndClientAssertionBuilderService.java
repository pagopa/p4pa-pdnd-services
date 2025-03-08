package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.utils.CertUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

@Service
public class PdndClientAssertionBuilderService {

  private final PdndApiClientConfig.PdndConfig pdndConfig;

  public PdndClientAssertionBuilderService(PdndApiClientConfig pdndApiClientConfig) {
    this.pdndConfig = pdndApiClientConfig.getConfig();
  }

  public String buildPdndClientAssertion(PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
    try {
      return buildAndSignPdndJWT(pdndServiceIntegratedConfig);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException | JOSEException e) {
      throw new IllegalStateException("Error building PDND client assertion", e);
    }
  }

  private JWTClaimsSet buildPdndClientAssertionClaims(String clientId, String purposeId) {
    long now = System.currentTimeMillis() / 1000;
    return new JWTClaimsSet.Builder()
        .issuer(clientId)
        .subject(clientId)
        .audience(pdndConfig.getAudience())
        .claim("purposeId",purposeId)
        .issueTime(new Date(now * 1000))
        .expirationTime(new Date((now + 300) * 1000))
        .jwtID(UUID.randomUUID().toString())
        .build();
  }

  private String buildAndSignPdndJWT(PdndServiceIntegratedConfig pdndServiceIntegratedConfig)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    JWTClaimsSet claims = buildPdndClientAssertionClaims(pdndServiceIntegratedConfig.getClientId(),
        pdndServiceIntegratedConfig.getPurposeId());
    JWSSigner signer = new RSASSASigner(CertUtils.pemKey2PrivateKey(pdndServiceIntegratedConfig.getPrivateKey()));
    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(pdndServiceIntegratedConfig.getKid())
            .build(),
        claims
    );
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }
}
