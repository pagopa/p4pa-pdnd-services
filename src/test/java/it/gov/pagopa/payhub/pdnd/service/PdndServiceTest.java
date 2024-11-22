package it.gov.pagopa.payhub.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.common.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import it.gov.pagopa.payhub.pdnd.exception.custom.JwtClaimBuildException;
import it.gov.pagopa.payhub.pdnd.model.PdndGenericConfig;
import it.gov.pagopa.payhub.pdnd.utils.JWTUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
    PdndGenericConfig config = Mockito.mock(PdndGenericConfig.class);
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
  void givenInvalidAssertionWhenGenerateTokenThenException() throws Exception {
    // Given
    PdndGenericConfig config = Mockito.mock(PdndGenericConfig.class);
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