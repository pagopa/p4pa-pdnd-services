package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClient;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AnprServiceTest {

  @Mock
  private PdndClient pdndClient;

  @Mock
  private PdndClientAssertionBuilderService pdndClientAssertionBuilderService;

  private PdndServiceImpl pdndService;

  @BeforeEach
  void setUp() {
    pdndService = new PdndServiceImpl(pdndClient, pdndClientAssertionBuilderService);
  }

  @Test
  void givenValidConfigWhenGenerateTokenThenGeneratesNewToken() {
    // Given
    PdndServiceIntegratedConfig serviceConfig = Mockito.mock(PdndServiceIntegratedConfig.class);
    String clientId = "CLIENTID";
    String clientAssertion = "ASSERTION";
    ClientCredentialsResponseDTO newAccessToken = new ClientCredentialsResponseDTO();

    // When
    Mockito.when(serviceConfig.getClientId()).thenReturn(clientId);
    Mockito.when(pdndClientAssertionBuilderService.buildPdndClientAssertion(serviceConfig)).thenReturn(clientAssertion);
    Mockito.when(pdndClient.getAccessToken(clientId, clientAssertion))
        .thenReturn(newAccessToken);

    String token = pdndService.generateToken(serviceConfig);

    // Then
    assertEquals(newAccessToken.getAccessToken(), token);
    Mockito.verify(pdndClientAssertionBuilderService, Mockito.times(1)).buildPdndClientAssertion(serviceConfig);
    Mockito.verify(pdndClient, Mockito.times(1)).getAccessToken(clientId, clientAssertion);
  }

  @Test
  void givenTokenInCacheWhenGenerateTokenThenReturnCachedToken() {
    // Given
    PdndServiceIntegratedConfig serviceConfig = Mockito.mock(PdndServiceIntegratedConfig.class);
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