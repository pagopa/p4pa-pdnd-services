package it.gov.pagopa.payhub.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.exception.custom.JwtClaimBuildException;
import it.gov.pagopa.payhub.pdnd.config.PdndBaseServiceIntegratedConfig;
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
    PdndBaseServiceIntegratedConfig config = Mockito.mock(PdndBaseServiceIntegratedConfig.class);
    String clientId = "CLIENTID";
    String clientAssertion = "ASSERTION";
    ClientCredentialsResponseDTO newAccessToken = new ClientCredentialsResponseDTO();

    // When
    Mockito.when(config.getClientId()).thenReturn(clientId);
    Mockito.when(pdndClientAssertionBuilderService.buildPdndClientAssertion(config)).thenReturn(clientAssertion);
    Mockito.when(pdndClientImpl.getAccessToken(clientId, clientAssertion))
        .thenReturn(newAccessToken);

    String token = pdndService.generateToken(config);

    // Then
    assertEquals(newAccessToken.getAccessToken(), token);
    Mockito.verify(pdndClientAssertionBuilderService, Mockito.times(1)).buildPdndClientAssertion(config);
    Mockito.verify(pdndClientImpl, Mockito.times(1)).getAccessToken(clientId, clientAssertion);
  }

  @Test
  void givenTokenInCacheWhenGenerateTokenThenReturnCachedToken() {
    // Given
    PdndBaseServiceIntegratedConfig config = Mockito.mock(PdndBaseServiceIntegratedConfig.class);
    String cachedToken = "CACHED_TOKEN";
    pdndService.jwtCache.put(config, cachedToken);

    try (MockedStatic<JWTUtils> mockedStatic = Mockito.mockStatic(JWTUtils.class)) {
      // When
      mockedStatic.when(() -> JWTUtils.isJWTExpired(cachedToken)).thenReturn(false);
      String token = pdndService.generateToken(config);

      // Then
      assertEquals(cachedToken, token);
    }
  }

  @Test
  void givenInvalidAssertionWhenGenerateTokenThenException() throws Exception {
    // Given
    PdndBaseServiceIntegratedConfig config = Mockito.mock(PdndBaseServiceIntegratedConfig.class);
    // When
    Mockito.when(pdndClientAssertionBuilderService.buildPdndClientAssertion(config))
        .thenThrow(new InvalidKeySpecException("Key spec error"));

    // Then
    JwtClaimBuildException exception = assertThrows(JwtClaimBuildException.class, () -> {
      pdndService.generateToken(config);
    });

    assertEquals("Error building JWT claims", exception.getMessage());
    assertInstanceOf(InvalidKeySpecException.class, exception.getCause());
  }

}