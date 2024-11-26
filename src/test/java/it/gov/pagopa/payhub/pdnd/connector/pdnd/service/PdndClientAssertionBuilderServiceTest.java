package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndBaseServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PdndClientAssertionBuilderServiceTest {


  @Mock
  private PdndConfig pdndConfig;

  @Mock
  private PdndBaseServiceIntegratedConfig pdndBaseServiceIntegratedConfig;

  @Mock
  private PdndServiceIntegrationConfig pdndServiceIntegrationConfig;

  @InjectMocks
  private PdndClientAssertionBuilderService pdndClientAssertionBuilderService;

  private String pemKey = """
  -----BEGIN PRIVATE KEY-----
    MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCT5fdA/ZKoyLas
    R5/kxfFm8KBz4v3i8k76Xd8j2vh8kBaapzn9hAHWJXOJ+GOUFOxkw1bnI2PUtZjj
    tw49XrjcxQ37sOV407+B3ko49zZjNB97OPFQyZx9V3uNcBjKnM3UqNbcBwdIIlVW
    Egt0Cao7gEGE1CKsaXpuZkofVgGo5f8K8IdETLJPFuspDTR4UPofDraL2HCxbsVx
    dE0UBFXgB9vQmBMkPk27cz+Ze6j5wgSGME/A+YCCp1uvJqWK/uRfGxMRyVYInR5H
    bDiI06iZwiLLW1Pf6gE3CCYSUw42VnPHODaitjJ6XLkolB5xsUprkttIg+UrRGSa
    9J3xg3gNAgMBAAECggEASKjRCS/KjntVK1xg1F7e0yjiWyyoeId8f4oApzfbni6X
    vFDtr3vb/x4VHjJWkZiZ7oL9Pb7oO8cfnrf/Ge1gOq3gycdFZU/6JM5VfpkNMj2Y
    Pcxi2cLCy91fyMPKmjfg81ojfKNDU4/yhr+EuvRImsTO63fgtP149aXxQmXZmOTu
    TFjSNTRfvtMgHN0Em1PUgQxO8oUh3Djf5spjAJ/w+gVBSYsYSv5sOOi2H/qZSALZ
    hc1t4GfzNKZuyG8FxNwH1SIVkKTYQnDhyiE9426tq6Kiuqvh2MspVJcRGpbaxgr2
    q++ZZrAl60ma5U2hUEgG5oLGjyrgQjEyroZhEokgLQKBgQDKIeAJ/FYdEX4cvHhS
    kuUpHQjpZtwOwC+vr4ojudpjLDOTTdkFXzd7jeCmjp4r1/arRxx1KZWP0fxlUEov
    0LDiaU0zBeol/q0ayq5XnhJNVngCyKjQQ+Np1eIGTIIGOkAm8LlnEsvlQLbuOYZ4
    eeeplBW3h321MFKgch7IyqBb5wKBgQC7UBG/ypw6RWPUOHYdtY1nLCQQJjvKCOMT
    DolkFB2UUuNfNGK6PDUL9KbPIsrHJLw0oGoqQyBkInVMG5jJb/bHdH0spiKGn51u
    orMk/xsA990Kqt+DT1Z5fEpoPchGMc529JR5h43n1n5s8/6jyDa5JNLFnS9xKZTm
    IvV/Nayt6wKBgGxpSs5QRqeEkE09UJOJMduhNPxqLLDEp07lKYQL1HPIa0kgQbu9
    2/YqnEj4ySDezfADTeIREaR3jZWRQJjwp05oB/3LuE/0jkeGWYeowkw0il2D3fcF
    0l0bWATk2AAbEflQtz/vNuiYkwSmWdcYGwY65ILw6p1Zc5eWXah39RYVAoGAI93Y
    GDZupcXFsMxC6btq4ReVrDX1+uCqwmplKnGjnFQmz4MTaH/A1JI7IqyR0YIaO6V/
    zqnd2O60MSeToPa8dUK7+UGymL6VgarLzMjAXfYYMEO52sXlVAvVn5I8+BvvYd3B
    VGf9ZyguOySZXLkoqVkAtvA7Nlr09QA6q+oWL5MCgYAsLS2PEMY/HMR1Z5P/uMxw
    q7eQ7K3YYKcJpbM2da7r38UaZc/HhtiaU/XOdTnT/M/eF4hoW0yxO5YKfgurgosz
    OjAnn7+Ed5S5Sh8E4EHUGCcawErZEZCtlsns0fNPGfNjadZAjq0X+5VP1EVXca0B
    VrSp9ZTif3cvyxNTOogbgA==
        -----END PRIVATE KEY-----
  """;

  @Test
  void givenValidPDNDConfigWhenBuildPdndClientAssertionThenVerifyToken() throws Exception {
    // Given
    Mockito.when(pdndConfig.getAudience()).thenReturn("AUDIENCE");
    Mockito.when(pdndBaseServiceIntegratedConfig.getClientId()).thenReturn("CLIENTID");
    Mockito.when(pdndBaseServiceIntegratedConfig.getKid()).thenReturn("KID");
    Mockito.when(pdndBaseServiceIntegratedConfig.getPrivateKey()).thenReturn(pemKey);
    Mockito.when(pdndServiceIntegrationConfig.getPurposeId()).thenReturn("PURPOSEID");

    // When
    String token = pdndClientAssertionBuilderService.buildPdndClientAssertion(
        pdndBaseServiceIntegratedConfig,
        pdndServiceIntegrationConfig);

    // Then
    assertNotNull(token);
  }
}