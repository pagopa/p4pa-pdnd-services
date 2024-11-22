package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.gov.pagopa.payhub.pdnd.config.PdndConfig;
import it.gov.pagopa.payhub.pdnd.model.PdndGenericConfig;
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

  public String buildPdndClientAssertion(PdndGenericConfig pdndGenericConfig)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    JWTClaimsSet claims = buildPdndClientAssertionClaims(pdndGenericConfig.getClientId(), pdndGenericConfig.getPurposeId());
    return signPdndJWT(pdndGenericConfig.getKid(), claims);
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

  private String signPdndJWT(String kid, JWTClaimsSet claims)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    JWSSigner signer = new RSASSASigner(CertUtils.pemKey2PrivateKey(pdndConfig.getKey()));
    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(kid)
            .build(),
        claims
    );
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }
}
