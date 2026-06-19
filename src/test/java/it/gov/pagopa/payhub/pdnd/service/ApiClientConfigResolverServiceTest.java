package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.send.connector.SendApiClientConfig;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ApiClientConfigResolverServiceTest {

  @Mock
  private SendApiClientConfig sendApiClientConfig;

  @Mock
  private AnprApiClientConfig anprApiClientConfig;

  @Mock
  private AnprApiClientConfig.AnprServicesConfig anprServices;

  @Mock
  private PdndServiceIntegratedStaticConfig sendConfig;

  @Mock
  private PdndServiceIntegratedStaticConfig c003ServiceConfig;

  @Mock
  private PdndServiceIntegratedStaticConfig c030ServiceConfig;

  private ApiClientConfigResolverService apiClientConfigResolverService;

  @BeforeEach
  void setUp() {
    apiClientConfigResolverService = new ApiClientConfigResolverService(sendApiClientConfig, anprApiClientConfig);
  }


  @Test
  void givenSENDServiceWhenGetIntegratedConfigThenReturnIntegratedConfig(){
    Mockito.when(sendApiClientConfig.getService()).thenReturn(sendConfig);
    PdndServiceIntegratedStaticConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServiceType.SEND);
    assertEquals(sendConfig, result);
  }

  @Test
  void givenC003ServiceWhenGetIntegratedConfigThenReturnIntegratedConfig() {
    Mockito.when(anprApiClientConfig.getServices()).thenReturn(anprServices);
    Mockito.when(anprServices.getC003()).thenReturn(c003ServiceConfig);
    PdndServiceIntegratedStaticConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServiceType.C003);
    assertEquals(c003ServiceConfig, result);
  }

  @Test
  void givenC030ServiceWhenGetIntegratedConfigThenReturnIntegratedConfig() {
    Mockito.when(anprApiClientConfig.getServices()).thenReturn(anprServices);
    Mockito.when(anprServices.getC030()).thenReturn(c030ServiceConfig);
    PdndServiceIntegratedStaticConfig result = apiClientConfigResolverService.getIntegratedConfig(PdndServiceType.C030);
    assertEquals(c030ServiceConfig, result);
  }
}