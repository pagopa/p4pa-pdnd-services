package it.gov.pagopa.payhub.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.dto.generated.PdndServicesEnum;
import it.gov.pagopa.payhub.pdnd.send.connector.SendApiClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiClientConfigResolverServiceTest {

  @Mock
  private SendApiClientConfig sendApiClientConfig;

  @Mock
  private AnprApiClientConfig anprApiClientConfig;

  @Mock
  private AnprApiClientConfig.AnprServicesConfig anprServices;

  @Mock
  private PdndServiceIntegratedConfig c003ServiceConfig;

  @Mock
  private PdndServiceIntegratedConfig c030ServiceConfig;

  private ApiClientConfigResolverService apiClientConfigResolverService;

  @BeforeEach
  void setUp() {
    apiClientConfigResolverService = new ApiClientConfigResolverService(sendApiClientConfig, anprApiClientConfig);
  }


  @Test
  void givenSENDServiceWhenGetIntegratedConfigThenReturnIntegratedConfig(){
    PdndServiceIntegratedConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServicesEnum.SEND);
    assertEquals(sendApiClientConfig.getService(), result);
  }

  @Test
  void givenC003ServiceWhenGetIntegratedConfigThenReturnIntegratedConfig() {
    Mockito.when(anprApiClientConfig.getServices()).thenReturn(anprServices);
    Mockito.when(anprServices.getC003()).thenReturn(c003ServiceConfig);
    PdndServiceIntegratedConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServicesEnum.C003);
    assertEquals(c003ServiceConfig, result);
  }

  @Test
  void givenC030ServiceWhenGetIntegratedConfigThenReturnIntegratedConfig() {
    Mockito.when(anprApiClientConfig.getServices()).thenReturn(anprServices);
    Mockito.when(anprServices.getC030()).thenReturn(c030ServiceConfig);
    PdndServiceIntegratedConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServicesEnum.C030);
    assertEquals(c030ServiceConfig, result);
  }
}