package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import it.gov.pagopa.payhub.pdnd.utils.CertUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PdndClientAssertionBuilderService {

  private final PdndConfig pdndConfig;

  public PdndClientAssertionBuilderService(PdndConfig pdndConfig) {
    this.pdndConfig = pdndConfig;
  }

  public String buildPdndClientAssertion(PdndServiceIntegrationConfig pdndServiceIntegrationConfig) {
    try {
      return buildAndSignPdndJWT(pdndServiceIntegrationConfig);
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

  private String buildAndSignPdndJWT(PdndServiceIntegrationConfig pdndServiceIntegrationConfig)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    JWTClaimsSet claims = buildPdndClientAssertionClaims(pdndServiceIntegrationConfig.getClientId(),
        pdndServiceIntegrationConfig.getPurposeId());
    JWSSigner signer = new RSASSASigner(CertUtils.pemKey2PrivateKey(pdndServiceIntegrationConfig.getPrivateKey()));
    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(pdndServiceIntegrationConfig.getKid())
            .build(),
        claims
    );
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }
}
