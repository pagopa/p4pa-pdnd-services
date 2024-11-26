package it.gov.pagopa.payhub.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import java.security.spec.InvalidKeySpecException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PdndServiceTest {

  @Mock
  private PdndClientImpl pdndClientImpl;

  @Mock
  private PdndClientAssertionBuilderService pdndClientAssertionBuilderService;

  private PdndService pdndService;

  @BeforeEach
  void setUp() {
    pdndService = new PdndService(pdndClientImpl, pdndClientAssertionBuilderService);
  }

  @Test
  void givenValidConfigWhenGenerateTokenThenGeneratesNewToken() throws Exception {
    // Given
    PdndServiceIntegrationConfig serviceConfig = Mockito.mock(PdndServiceIntegrationConfig.class);
    String clientId = "CLIENTID";
    String clientAssertion = "ASSERTION";
    ClientCredentialsResponseDTO newAccessToken = new ClientCredentialsResponseDTO();

    // When
    Mockito.when(serviceConfig.getClientId()).thenReturn(clientId);
    Mockito.when(pdndClientAssertionBuilderService.buildPdndClientAssertion(serviceConfig)).thenReturn(clientAssertion);
    Mockito.when(pdndClientImpl.getAccessToken(clientId, clientAssertion))
        .thenReturn(newAccessToken);

    String token = pdndService.generateToken(serviceConfig);

    // Then
    assertEquals(newAccessToken.getAccessToken(), token);
    Mockito.verify(pdndClientAssertionBuilderService, Mockito.times(1)).buildPdndClientAssertion(serviceConfig);
    Mockito.verify(pdndClientImpl, Mockito.times(1)).getAccessToken(clientId, clientAssertion);
  }

  @Test
  void givenTokenInCacheWhenGenerateTokenThenReturnCachedToken() {
    // Given
    PdndServiceIntegrationConfig serviceConfig = Mockito.mock(PdndServiceIntegrationConfig.class);
    String cachedToken = "CACHED_TOKEN";
    pdndService.jwtCache.put(serviceConfig, cachedToken);

    try (MockedStatic<JWTUtils> mockedStatic = Mockito.mockStatic(JWTUtils.class)) {
      // When
      mockedStatic.when(() -> JWTUtils.isJWTExpired(cachedToken)).thenReturn(false);
      String token = pdndService.generateToken(serviceConfig);

      // Then
      assertEquals(cachedToken, token);
    }
  }

}