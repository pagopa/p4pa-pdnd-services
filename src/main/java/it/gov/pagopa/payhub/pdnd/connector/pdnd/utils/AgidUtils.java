package it.gov.pagopa.payhub.pdnd.connector.pdnd.utils;

import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.utils.CryptoUtils;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class AgidUtils {
    private AgidUtils() {
    }

    private static final String CONTENT_ENCODING_LOWERCASE = HttpHeaders.CONTENT_ENCODING.toLowerCase();
    private static final String CONTENT_TYPE_LOWERCASE = HttpHeaders.CONTENT_TYPE.toLowerCase();
    private static final Random random = new SecureRandom();

    public static String buildPdndClientAssertion(PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig, String agidJwtTrackingEvidence, RSASSASigner rsaJwsSigner) {
        return JWTUtils.signJwtRSA(
                        buildPdndClientAssertionClaims(pdndConfig, pdndServiceIntegratedConfig, agidJwtTrackingEvidence),
                        pdndServiceIntegratedConfig.getKid(),
                        rsaJwsSigner)
                .serialize();
    }

    private static JWTClaimsSet buildPdndClientAssertionClaims(PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig, String agidJwtTrackingEvidence) {
        String clientId = pdndServiceIntegratedConfig.getClientId();
        String purposeId = pdndServiceIntegratedConfig.getPurposeId();
        long currentMillis = System.currentTimeMillis();
        long expirationMillis = currentMillis + (pdndConfig.getAuthExpirationMinutes() * 60 * 1000);

        return new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer(clientId)
                .subject(clientId)
                .audience(pdndConfig.getAudience())
                .issueTime(new Date(currentMillis))
                .expirationTime(new Date(expirationMillis))
                .claim("purposeId", purposeId)
                .claim("digest", Map.of(
                        "alg", "SHA256",
                        "value", CryptoUtils.sha256HEX(agidJwtTrackingEvidence)
                ))
                .build();
    }

    public static String buildAgidJwtTrackingEvidence(PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig, RSASSASigner rsaJwsSigner) {
        return JWTUtils.signJwtRSA(
                        buildAgidJwtTrackingEvidenceClaims(pdndConfig, pdndServiceIntegratedConfig),
                        pdndServiceIntegratedConfig.getKid(),
                        rsaJwsSigner)
                .serialize();
    }

    private static JWTClaimsSet buildAgidJwtTrackingEvidenceClaims(PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
        String clientId = pdndServiceIntegratedConfig.getClientId();
        String purposeId = pdndServiceIntegratedConfig.getPurposeId();
        long currentMillis = System.currentTimeMillis();
        long expirationMillis = currentMillis + (pdndConfig.getAuthExpirationMinutes() * 60 * 1000);

        return new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer(clientId)
                .audience(pdndConfig.getAudience())
                .issueTime(new Date(currentMillis))
                .expirationTime(new Date(expirationMillis))
                .claim("purposeId", purposeId)
                .claim("dnonce", "%013d".formatted(random.nextLong(9999999999999L)))
                .claim("userLocation", pdndConfig.getEnv())
                .claim("userID", pdndConfig.getUserId())
                .claim("LoA", "LOA3")
                .build();
    }

    public static String buildDigest(String value) {
        return "SHA-256=" + CryptoUtils.sha256Base64(value);
    }

    public static String buildAgidJwtSignature(String digest, PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig, RSASSASigner rsaJwsSigner) {
        return JWTUtils.signJwtRSA(
                        buildAgidJwtSignatureClaims(digest, pdndConfig, pdndServiceIntegratedConfig),
                        pdndServiceIntegratedConfig.getKid(),
                        rsaJwsSigner)
                .serialize();
    }

    private static JWTClaimsSet buildAgidJwtSignatureClaims(String digest, PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
        String clientId = pdndServiceIntegratedConfig.getClientId();
        long currentMillis = System.currentTimeMillis();
        long expirationMillis = currentMillis + (pdndConfig.getAuthExpirationMinutes() * 60 * 1000);

        return new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer(clientId)
                .subject(clientId)
                .audience(pdndConfig.getAudience())
                .issueTime(new Date(currentMillis))
                .expirationTime(new Date(expirationMillis))
                .claim("signed_headers", List.of(
                                Map.of("digest", digest),
                                Map.of(CONTENT_ENCODING_LOWERCASE, StandardCharsets.UTF_8.name()),
                                Map.of(CONTENT_TYPE_LOWERCASE, MediaType.APPLICATION_JSON_VALUE)
                        )
                )
                .build();
    }
}
