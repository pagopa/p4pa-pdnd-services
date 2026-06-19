package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClient;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.AgidUtils;
import it.gov.pagopa.payhub.pdnd.utils.CertUtils;
import it.gov.pagopa.payhub.pdnd.utils.CertUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class PdndAuthDataBuilderServiceTest {

    @Mock
    private PdndClient pdndClientMock;
    private final PdndApiClientConfig.PdndConfig pdndConfig = PdndApiClientConfig.PdndConfig.builder()
            .env("DEV")
            .userId("USERID")
            .authExpirationMinutes(1)
            .audience("PDNDAUDIENCE")
            .build();

    private PdndAuthDataBuilderService service;

    @BeforeEach
    void init() {
        PdndApiClientConfig apiConfig = PdndApiClientConfig.builder()
                .config(pdndConfig)
                .build();

        service = new PdndAuthDataBuilderService(apiConfig, pdndClientMock);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(pdndClientMock);
    }

    @Test
    void whenBuildThenOk() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        // Given
        PrivateKey signerPrivateKey = CertUtils.pemKey2PrivateKey(CertUtilsTest.PRIVATE_KEY);
        PdndServiceIntegratedConfig pdndServiceConfig = PdndServiceIntegratedConfig.builder()
                .kid("KID")
                .clientId("CLIENTID")
                .purposeId("PURPOSEID")
                .privateKey(CertUtilsTest.PRIVATE_KEY)
                .publicKey(CertUtilsTest.PUBLIC_KEY)
                .build();

        try (MockedStatic<AgidUtils> agidUtilsMockedStatic = Mockito.mockStatic(AgidUtils.class)) {
            String agidJwtTrackingEvidence = "AGIDJWTTRACKINGEVIDENCE";
            String clientAssertion = "CLIENTASSERTION";
            String accessToken = "ACCESSTOKEN";
            int accessTokenExpiresIn = 60;

            ArgumentMatcher<RSASSASigner> signerMatcher = s -> s.getPrivateKey().equals(signerPrivateKey);
            agidUtilsMockedStatic.when(() -> AgidUtils.buildAgidJwtTrackingEvidence(Mockito.same(pdndConfig), Mockito.same(pdndServiceConfig), Mockito.argThat(signerMatcher)))
                    .thenReturn(agidJwtTrackingEvidence);
            agidUtilsMockedStatic.when(() -> AgidUtils.buildPdndClientAssertion(Mockito.same(pdndConfig), Mockito.same(pdndServiceConfig), Mockito.same(agidJwtTrackingEvidence), Mockito.argThat(signerMatcher)))
                    .thenReturn(clientAssertion);

            Mockito.when(pdndClientMock.getAccessToken(pdndServiceConfig.getClientId(), clientAssertion))
                    .thenReturn(new ClientCredentialsResponseDTO(accessToken, null, accessTokenExpiresIn));

            LocalDateTime dateTimeBeforeTest = LocalDateTime.now();

            // When
            PdndAuthData result = service.build(pdndServiceConfig);

            // Then
            Assertions.assertTrue(result.getExpiration().isAfter(dateTimeBeforeTest));
            Assertions.assertFalse(result.getExpiration().isAfter(LocalDateTime.now().plusMinutes(pdndConfig.getAuthExpirationMinutes())));
            Assertions.assertTrue(result.getExpiration().isBefore(dateTimeBeforeTest.plusMinutes(accessTokenExpiresIn)));

            Assertions.assertEquals(signerPrivateKey, result.getRsaJwsSigner().getPrivateKey());

            PdndAuthData expectedResult = new PdndAuthData(
                    agidJwtTrackingEvidence,
                    clientAssertion,
                    accessToken,
                    result.getExpiration(),
                    pdndServiceConfig,
                    pdndServiceConfig.getAudience(),
                    result.getRsaJwsSigner()
            );

            Assertions.assertEquals(expectedResult, result);
        }
    }
}
