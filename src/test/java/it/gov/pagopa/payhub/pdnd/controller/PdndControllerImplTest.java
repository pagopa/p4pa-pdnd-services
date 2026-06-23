package it.gov.pagopa.payhub.pdnd.controller;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.SecurityUtilsTest;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;

@ExtendWith(MockitoExtension.class)
class PdndControllerImplTest {

  @Mock
  private PdndService pdndServiceMock;

  @InjectMocks
  private PdndControllerImpl pdndController;
  private final String accessToken = "fakeAccessToken";
  private final String userId = "USERID";

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, userId);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(pdndServiceMock);
  }

  @Test
  void givenValidServiceWhenGetVoucherTokenThenReturnPdndAuthData() {
    PdndServiceType service = PdndServiceType.SEND;
    Long organizationId = 1L;
    String subUnitCode = "subUnitCode";
    PdndServiceIntegratedConfig pdndServiceIntegratedConfig = PdndServiceIntegratedConfig.builder()
        .clientId("clientId")
        .audience("serviceAudience")
        .kid("kid")
        .build();
    PdndAuthData mockAuthData = new PdndAuthData("JWTEVIDANCE",
        "ASSERTION","TOKEN",
        LocalDateTime.of(2026, Month.JUNE, 18, 12, 0),
            pdndServiceIntegratedConfig,
            pdndServiceIntegratedConfig.getAudience(),
            null
            );

    Mockito.when(pdndServiceMock.generateToken(service,organizationId,subUnitCode, accessToken)).thenReturn(mockAuthData);

    ResponseEntity<PdndAuthData> response = pdndController.getVoucherToken(service,organizationId,subUnitCode);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertEquals(mockAuthData, response.getBody());
  }
}