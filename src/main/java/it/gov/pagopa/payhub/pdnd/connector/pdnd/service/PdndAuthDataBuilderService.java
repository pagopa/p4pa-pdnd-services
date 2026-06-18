package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClient;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.AgidUtils;
import it.gov.pagopa.payhub.pdnd.utils.CertUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@Service
public class PdndAuthDataBuilderService {

    private final PdndApiClientConfig.PdndConfig pdndConfig;
    private final PdndClient pdndClient;

    public PdndAuthDataBuilderService(
            PdndApiClientConfig pdndApiClientConfig, PdndClient pdndClient) {
        this.pdndConfig = pdndApiClientConfig.getConfig();
        this.pdndClient = pdndClient;
    }

    public PdndAuthData build(PdndServiceIntegratedConfig pdndServiceIntegratedConfig){
        RSASSASigner signer = buildJwsSigner(pdndServiceIntegratedConfig);
        LocalDateTime now = LocalDateTime.now();

        String agidJwtTrackingEvidence = AgidUtils.buildAgidJwtTrackingEvidence(pdndConfig, pdndServiceIntegratedConfig, signer);
        String clientAssertion = AgidUtils.buildPdndClientAssertion(pdndConfig, pdndServiceIntegratedConfig, agidJwtTrackingEvidence, signer);
        ClientCredentialsResponseDTO pdndCredentials = pdndClient.getAccessToken(pdndServiceIntegratedConfig.getClientId(), clientAssertion);

        return new PdndAuthData(
                agidJwtTrackingEvidence,
                clientAssertion,
                pdndCredentials.getAccessToken(),
                now.plusMinutes(Math.min(pdndConfig.getAuthExpirationMinutes(), pdndCredentials.getExpiresIn())),
                pdndServiceIntegratedConfig.getClientId(),
                pdndServiceIntegratedConfig.getAudience(),
                pdndServiceIntegratedConfig.getKid(),
                pdndServiceIntegratedConfig.getBasePath(),
                signer
                );
    }

    private RSASSASigner buildJwsSigner(PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
        try {
            return new RSASSASigner(CertUtils.pemKey2PrivateKey(pdndServiceIntegratedConfig.getPrivateKey()));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            throw new IllegalStateException("Cannot build JWS Signer for PDND service having clientId:" + pdndServiceIntegratedConfig.getClientId(), e);
        }
    }
}
