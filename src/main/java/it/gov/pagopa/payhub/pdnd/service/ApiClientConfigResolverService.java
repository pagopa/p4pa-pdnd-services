package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.dto.generated.PdndServicesEnum;
import it.gov.pagopa.payhub.pdnd.send.connector.SendApiClientConfig;
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

  public PdndServiceIntegratedConfig getIntegratedConfig(PdndServicesEnum service){
    return switch (service) {
      case PdndServicesEnum.SEND -> sendApiClientConfig.getService();
      case PdndServicesEnum.C003 -> anprApiClientConfig.getServices().getC003();
      case PdndServicesEnum.C030 -> anprApiClientConfig.getServices().getC030();
    };
  }
}
