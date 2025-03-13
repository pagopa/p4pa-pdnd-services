package it.gov.pagopa.payhub.pdnd.controller;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.dto.generated.PdndServicesEnum;
import it.gov.pagopa.payhub.pdnd.service.ApiClientConfigResolverService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PdndControllerImplTest {

  @Mock
  private PdndService pdndService;

  @Mock
  private ApiClientConfigResolverService apiClientConfigResolverService;

  @InjectMocks
  private PdndControllerImpl pdndController;



  @Test
  void givenValidServiceWhenGetVoucherTokenThenReturnPdndAuthData() {
    PdndServicesEnum service = PdndServicesEnum.SEND;
    PdndAuthData mockAuthData = new PdndAuthData("JWTEVIDANCE",
        "ASSERTION","TOKEN",
        LocalDateTime.now(),
        null);

    PdndServiceIntegratedConfig mockConfig = new PdndServiceIntegratedConfig();

    Mockito.when(apiClientConfigResolverService.getIntegratedConfig(service)).thenReturn(mockConfig);
    Mockito.when(pdndService.generateToken(mockConfig)).thenReturn(mockAuthData);

    ResponseEntity<PdndAuthData> response = pdndController.getVoucherToken(service);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertEquals(mockAuthData, response.getBody());
  }
}