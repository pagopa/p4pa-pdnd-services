package it.gov.pagopa.payhub.pdnd.utils;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.gov.pagopa.payhub.pdnd.config.PdndConfig;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PdndUtils {

  private final PdndConfig pdndConfig;

  public PdndUtils(PdndConfig pdndConfig) {
    this.pdndConfig = pdndConfig;
  }

  public String buildPdndClientAssertion()
      throws Exception {
    JWTClaimsSet claims = buildPdndClientAssertionClaims();
    return signPdndJWT(claims);
  }

  public JWTClaimsSet buildPdndClientAssertionClaims() {
    long now = System.currentTimeMillis() / 1000;
    return new JWTClaimsSet.Builder()
        .issuer(pdndConfig.getClientId())
        .subject(pdndConfig.getClientId())
        .audience(pdndConfig.getAudience())
        .claim("purposeId",pdndConfig.getPurposeId())
        .issueTime(new Date(now * 1000))
        .expirationTime(new Date((now + 300) * 1000))
        .jwtID(UUID.randomUUID().toString())
        .build();
  }

  public String signPdndJWT(JWTClaimsSet claims) throws Exception {
    JWSSigner signer = new RSASSASigner(CertUtils.pemKey2PrivateKey(pdndConfig.getKey()));
    SignedJWT signedJWT = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .keyID(pdndConfig.getKid())
            .build(),
        claims
    );
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }
}
