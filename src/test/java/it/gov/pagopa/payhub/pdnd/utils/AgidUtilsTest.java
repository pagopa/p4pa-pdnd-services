package it.gov.pagopa.payhub.pdnd.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.json.JsonAssert;
import org.springframework.test.json.JsonCompareMode;

import java.util.List;
import java.util.Map;

public class AgidUtilsTest {

    public static RSASSASigner signer;

    static {
        try {
            signer = new RSASSASigner(CertUtils.pemKey2PrivateKey(CertUtilsTest.PRIVATE_KEY));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final PdndApiClientConfig.PdndConfig pdndConfig = PdndApiClientConfig.PdndConfig.builder()
            .audience("PDNDAUDIENCE")
            .authExpirationMinutes(1)
            .userId("USERID")
            .env("DEV")
            .build();

    private final PdndServiceIntegratedConfig pdndServiceIntegratedConfig = PdndServiceIntegratedConfig.builder()
            .clientId("CLIENTID")
            .kid("KID")
            .purposeId("PURPOSEID")
            .build();

    @Test
    void whenBuildPdndClientAssertionThenOk() {
        // Given
        String agidJwtTrackingEvidence = "TRACKINGJWT";

        // When
        String token = AgidUtils.buildPdndClientAssertion(
                pdndConfig,
                pdndServiceIntegratedConfig,
                agidJwtTrackingEvidence,
                signer);

        // Then
        DecodedJWT decoded = JWT.decode(token);
        JsonAssert.comparator(JsonCompareMode.STRICT).assertIsMatch(
                Map.of(
                        RegisteredClaims.JWT_ID, decoded.getClaim(RegisteredClaims.JWT_ID),
                        RegisteredClaims.SUBJECT, pdndServiceIntegratedConfig.getClientId(),
                        RegisteredClaims.ISSUER, pdndServiceIntegratedConfig.getClientId(),
                        RegisteredClaims.AUDIENCE, pdndConfig.getAudience(),
                        RegisteredClaims.ISSUED_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT),
                        RegisteredClaims.EXPIRES_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT).asLong() + 60,
                        "purposeId", pdndServiceIntegratedConfig.getPurposeId(),
                        "digest", Map.of(
                                "alg", "SHA256",
                                "value", CryptoUtils.sha256HEX(agidJwtTrackingEvidence)
                        )).toString(),
                decoded.getClaims().toString());
    }

    @Test
    void whenBuildAgidJwtTrackingEvidenceThenOk() {
        // When
        String token = AgidUtils.buildAgidJwtTrackingEvidence(
                pdndConfig,
                pdndServiceIntegratedConfig,
                signer);

        // Then
        DecodedJWT decoded = JWT.decode(token);
        JsonAssert.comparator(JsonCompareMode.STRICT).assertIsMatch(
                Map.of(
                        RegisteredClaims.JWT_ID, decoded.getClaim(RegisteredClaims.JWT_ID),
                        RegisteredClaims.ISSUER, pdndServiceIntegratedConfig.getClientId(),
                        RegisteredClaims.AUDIENCE, pdndConfig.getAudience(),
                        RegisteredClaims.ISSUED_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT),
                        RegisteredClaims.EXPIRES_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT).asLong() + 60,
                        "purposeId", pdndServiceIntegratedConfig.getPurposeId(),
                        "dnonce", decoded.getClaim("dnonce"),
                        "userLocation", pdndConfig.getEnv(),
                        "userID", pdndConfig.getUserId(),
                        "LoA", "LOA3"
                ).toString(),
                decoded.getClaims().toString());
    }

    @Test
    void whenBuildAgidJwtSignatureThenOk() {
        // Given
        String digest = "DIGEST";

        // When
        String token = AgidUtils.buildAgidJwtSignature(
                digest,
                pdndConfig,
                pdndServiceIntegratedConfig,
                signer);

        // Then
        DecodedJWT decoded = JWT.decode(token);
        JsonAssert.comparator(JsonCompareMode.STRICT).assertIsMatch(
                Map.of(
                        RegisteredClaims.JWT_ID, decoded.getClaim(RegisteredClaims.JWT_ID),
                        RegisteredClaims.SUBJECT, pdndServiceIntegratedConfig.getClientId(),
                        RegisteredClaims.ISSUER, pdndServiceIntegratedConfig.getClientId(),
                        RegisteredClaims.AUDIENCE, pdndConfig.getAudience(),
                        RegisteredClaims.ISSUED_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT),
                        RegisteredClaims.EXPIRES_AT, decoded.getClaim(RegisteredClaims.ISSUED_AT).asLong() + 60,
                        "signed_headers", List.of(
                                Map.of("digest", digest),
                                Map.of("\"content-encoding\"", "\"UTF-8\""),
                                Map.of("\"content-type\"", "\"application/json\"")
                        )
                ).toString(),
                decoded.getClaims().toString());
    }

    @Test
    void whenBuildDigestThenOk(){
        Assertions.assertEquals(
                "SHA-256=f9SGUoWD/kZFYdz81VpXWA9SCqyEw0hZXvnSdwuRRG8=",
                AgidUtils.buildDigest("PROVA")
        );
    }
}