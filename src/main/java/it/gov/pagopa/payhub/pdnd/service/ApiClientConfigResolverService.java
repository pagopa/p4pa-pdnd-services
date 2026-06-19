package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.send.connector.SendApiClientConfig;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class ApiClientConfigResolverService {

  private final SendApiClientConfig sendApiClientConfig;
  private final AnprApiClientConfig anprApiClientConfig;

  public ApiClientConfigResolverService(SendApiClientConfig sendApiClientConfig,
      AnprApiClientConfig anprApiClientConfig) {
    this.sendApiClientConfig = sendApiClientConfig;
    this.anprApiClientConfig = anprApiClientConfig;
  }

  public PdndServiceIntegratedStaticConfig getIntegratedConfig(PdndServiceType service){
    PdndServiceIntegratedStaticConfig pdndServiceIntegratedStaticConfig = switch (service) {
      case PdndServiceType.SEND -> sendApiClientConfig.getService();
      case PdndServiceType.ANPR_C003 -> anprApiClientConfig.getServices().getC003();
      case PdndServiceType.ANPR_C030 -> anprApiClientConfig.getServices().getC030();
    };
    if(pdndServiceIntegratedStaticConfig == null){
      throw new NotImplementedException("Missing static configuration for serviceType "+service);
    }
    return pdndServiceIntegratedStaticConfig;
  }
}
